package services

import java.io.File
import javax.script.ScriptEngineManager

import models.AutocompleteRequest

import scala.reflect.internal.util.{OffsetPosition, BatchSourceFile}
import scala.reflect.io.AbstractFile
import scala.tools.nsc.interactive.{Response, Global}
import scala.tools.nsc.reporters.StoreReporter

/**
 * Created by martin on 08/04/15.
 */
object CompilerServiceObj extends CompilerService

class CompilerService {
  val debug = true
  val engine = new ScriptEngineManager().getEngineByName("scala")
  val settings = engine.asInstanceOf[scala.tools.nsc.interpreter.IMain].settings
  // MyScalaClass is just any class in your project
  settings.embeddedDefaults[CompilerService]
  settings.usejavacp.value = true

  val reporter = new StoreReporter()
  val compiler = new Global(settings, reporter)

  //TODO: It should cancel current operation if autocomplete is asked again.
  //TODO: Routes should be extracted from configuration file probably.

  def complete(request: AutocompleteRequest): String ={
    reloadAll(request.path.trim+"/src/main/scala/")
    //Load file to analize
    val filePath = request.path.trim+"/src/main/scala/"+request.fileName;
    val myFile = AbstractFile.getFile(filePath)
    if(myFile==null) return ""
    val content = scala.io.Source.fromFile(filePath).getLines.toList
    val absolutePos= (content.take(request.line).foldLeft(0)(_+_.length)+request.line+request.offset)
    val formattedContent = content.mkString("\n")
    if(debug) println(formattedContent.take(absolutePos))
    val myBsf = new BatchSourceFile(myFile, formattedContent)
    val pos = new OffsetPosition(myBsf, absolutePos)

    compiler.ask {
      ()=>
        val response = new Response[List[compiler.Member]]
        //Twice because Scala Presentation compiler sucks ???
        compiler.askTypeCompletion(pos, response)

        response.get(60*1000).map {
          //case Left(lst) => lst.map(x=> ("sym"->x.sym.toString().drop("method ".length))~("types"->x.tpe.toString()))
          case Left(lst) => lst.map(x=> ("sym"->x.sym.toString().drop("method ".length))).mkString
          case Right(e) => ""
        }.getOrElse("")
    }
  }

  private def reloadAll(path:String): Unit ={
    //Reload all files in proyect
    val files = new File(path.trim).listFiles().filter(_.isFile).filter(_.getName.contains(".scala"))
    val allFiles =  files.map(f=>new BatchSourceFile(AbstractFile.getFile(f.getAbsolutePath), scala.io.Source.fromFile(f.getAbsolutePath).getLines.toList.mkString("\n"))).toList


    compiler.ask{
      ()=>
        //Reloading
        val r = new Response[Unit]
        compiler.askReload(allFiles, r)
        r.get
        //Doing shit...
        val ask = compiler.askStructure(true) _
        allFiles.map{ x=>
          val r = new Response[compiler.Tree]()
          ask(x, r)
          r.get
        }
    }

  }

}
