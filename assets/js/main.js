
var fs = require('fs');
var wrench = require('wrench');
var path = require('path');

require('nw.gui').Window.get().showDevTools() 

$( document ).ready( function(){
    var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
        lineNumbers: true,
        matchBrackets: true,
        theme: "base16-light",
        mode: "text/x-scala"
      });

//CodeMirror.hint.javascript = function(cm) {
 // var inner = orig(cm) || {from: cm.getCursor(), to: cm.getCursor(), list: []};
  //inner.list.push("bozo");
  //return inner.
//};
    
	editor.setOption("extraKeys", {
	  "Ctrl-Space": function(cm) {
	    editor.showHint("olaaaa");
            var cursor = editor.getCursor()
            var params = { line: cursor.line, offset: cursor.ch , path: path.resolve(newName)+"/", fileName: "MyApp.scala"};
            $.get( "http://localhost:9000/codeComplete", params, 
		function(res){
		            console.log(res);		
		} );
            console.log(editor.getCursor());
	  }
	});


    var newName;
    var myApp = '/src/main/scala/MyApp.scala';
    
    $('#create-sketch').click(function() {
        newName = '../templates/'+$('#new-sketch-name').val();
        wrench.copyDirSyncRecursive('../templates/main-template', newName);
        fs.readFile(newName+myApp,function (err, data) {
            editor.setValue(data.toString());
        });    
            
            //Escondo y desactivo todo lo demas
            $('#main-menu').children().removeClass("active");
            $('#content').children().hide();
            //Muestro lo que quiero
            $('#code-editor').show(500);
            $('#new-sketch').addClass("active");
            $('#new-sketch-modal').modal('hide');
    });
    
        
    var openBrowser = function(error, stdout, stderr){ 
            var proc2 = exec("x-www-browser "+newName+"/index.html");
        };

    $('#compile').click(function() {
        fs.writeFile(newName+myApp, editor.getValue());
        
        //alert(newName);
        
        var exec = require('child_process').exec;
        var proc = exec("cd "+newName+" && sbt clean compile fastOptJS", function(error, stdout, stderr){alert("FINISHED")});
    });
    
   
    $('#preview').click(function() {
        var exec = require('child_process').exec;
        alert("x-www-browser "+newName+"index.html")
        var proc2 = exec("x-www-browser "+newName+"/index.html");     
    });
});
                                    
                                    
