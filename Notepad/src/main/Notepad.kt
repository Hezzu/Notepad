package main.notepad

import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCombination
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.io.IOException

import java.io.PrintWriter
import java.util.logging.Level
import java.util.logging.Logger

//Ignore null for this var file fuc*er... Details later
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Notepad : Application() {
    //Function to create a new unthemed window... It's Highly flexible
    private fun openNewWindow(infoText: String, title: String)
    {
        //As in this app new windows will be mostly if not only informative windows, I predefined label as part of new window function
        val info = Label(infoText)
        //Padding and alignment obviously for centered and responsive design
        info.padding = Insets(5.0)
        info.alignment = Pos.CENTER
        //Creating a Scene and Stage for new window
        val scene = Scene(info, 200.0, 100.0)
        val newWindow = Stage()
        //Giving new window a title from arguments and setting its resizable feature to false and setting stage's Scene to scene val
        newWindow.title = title
        newWindow.scene = scene
        newWindow.isResizable = false
        //Giving new window a size and finally showing it
        newWindow.width = 300.0
        newWindow.height = 200.0
        newWindow.show()
    }
    override fun start(stage: Stage) {
        //Creating a layout, scene and main text Area as it's notepad
        val vBox = VBox()
        val scene = Scene(vBox, 640.0, 480.0)
        val textArea = TextArea()

        //Creating a file chooser for later use in save and open
        val fileChooser = FileChooser()
        //Giving a default name to saved files
        fileChooser.initialFileName = "Note.txt"
        //Giving file chooser an extension hint
        val extFilter = FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt")
        fileChooser.extensionFilters.add(extFilter)
        //I fought a lot with this fucker... nullable variable that gives file saved or opened. Its nullable as you can save before picking a file
        var file: File? = null

        //Creating a Menu bar where Menu items will be placed
        val menuBar = MenuBar()

        //Creating file menu items and its subitems
        val fileMenu = Menu("File")

        val saveFile = MenuItem("Save file")
        //Giving saveFile an action listener
        saveFile.setOnAction {
            //Try-Catch for errors when canceling file chooser
            try {
                //if that checks value of file and opens a save dialog when it has no value
                if(file == null)
                {
                    file = fileChooser.showSaveDialog(stage)
                }
                val writer = PrintWriter(file)
                writer.println(textArea.text)
                writer.close()
                //Changing window name so it contains file name in it
                stage.title = "${file!!.name} : ${stage.title}"
            }
            catch (ex: IOException) {
                Logger.getLogger(Notepad::class.java.name).log(Level.SEVERE, null, ex)
            }
        }
        //Giving subitem a keyboard shortcut
        saveFile.accelerator = KeyCombination.keyCombination("Ctrl+S")

        //Same thing as in saveFile but this time for Opening them
        val openFile = MenuItem("Open a File")
        openFile.accelerator = KeyCombination.keyCombination("Ctrl+O")
        openFile.setOnAction {
           try{
                fileChooser.title = "Select file to open"
                file = fileChooser.showOpenDialog(stage)
               //Setting text from file to text area
                textArea.text = file!!.readText()
                stage.title = "${file!!.name} : ${stage.title}"
            }
           catch(ex: IOException) {
                   Logger.getLogger(Notepad::class.java.name).log(Level.SEVERE, null, ex)
           }
        }
        //Adding all subitems to menu item
        fileMenu.items.addAll(openFile, saveFile)

        //Adding edit menu, its items and subitems
        val editMenu = Menu("Edit")
        //Adding format menu, its items and subitems
        val formatMenu = Menu("Format")
        //Adding view menu, its items and subitems
        val viewMenu = Menu("View")
        //Adding about menu, its items and subitems
        val aboutMenu = Menu("About")
        //Creating a credits' subitem
        val ownerButton = MenuItem("Creator")
        //Action listener that opens new window with my info in it
        ownerButton.setOnAction { openNewWindow("Creator of this Notepad is Hezzu", "Creator") }
        //Creating subitem to view windows size
        val showSizeButton = MenuItem("Show Window size")
        showSizeButton.setOnAction { openNewWindow("Width: ${stage.width}\nHeight: ${stage.height} ", "Parameters") }
        //Adding subitems to menu item and then items to menu bar
        aboutMenu.items.addAll(ownerButton, showSizeButton)
        menuBar.menus.addAll(fileMenu, editMenu, formatMenu, viewMenu, aboutMenu)

        //Adding menu bar and main text area to layout
        vBox.children.addAll(menuBar, textArea)
        //Setting max height, max width to make text area fill whole space (might be incompatible with 1920+ resolution devices)
        textArea.maxHeight = 1920.0
        textArea.prefHeight = 1920.0
        vBox.maxHeight = 1920.0
        //Adding padding so its somehow responsive and minimum stage resolution so menu won't hide etiquettes
        vBox.padding = Insets(5.0, 5.0, 5.0, 5.0)
        stage.minHeight = 120.0
        stage.minWidth = 270.0
        //Giving menu a cool rounded black border
        menuBar.border = Border(BorderStroke(
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
            BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
            CornerRadii(5.0, 5.0, 0.0, 0.0, false), BorderWidths(2.0, 2.0, 1.0, 2.0), Insets.EMPTY))
        //Making CSS for menu, text area and layout colors
        menuBar.style = """
            -fx-background-radius: 5;
            -fx-background-color: grey;
        """.trimIndent()
        textArea.style = """
            -fx-background-radius: 5;
        """.trimIndent()
        vBox.style = "-fx-background-color: #474747;"
        //Making cool border for text area
        textArea.border = Border(BorderStroke(
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
            BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
            CornerRadii(0.0, 0.0, 5.0, 5.0, false), BorderWidths(1.0, 2.0, 2.0, 2.0), Insets.EMPTY))

        //Giving a title, setting a scene of stage and showing it
        stage.title = "Crappy Notepad!"
        stage.scene = scene
        stage.show()
    }
}
//Function to launch application
fun main() {
    Application.launch(Notepad::class.java)
}