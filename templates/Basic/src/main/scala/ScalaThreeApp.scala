import org.scalajs.dom._
import org.scalajs.jquery._
import scala.scalajs.js

object ScalaThreeApp extends js.JSApp{

  val renderer = new WebGLRenderer()

  def getRenderer:WebGLRenderer={
	return renderer
  }

  def main() = {
    
    renderer.setSize(window.innerWidth, window.innerHeight) //TODO: Que se pueda settear desde "afuera"
    jQuery("body").append(renderer.domElement)

    MyApp.setup()

    def render()={
      MyApp.render()
      renderer.render(MyApp.scene, MyApp.camera)
    }

    def mainRender(){
      render()
      window.requestAnimationFrame( (a:Double) => mainRender() );
    }

    mainRender()
  }
}
