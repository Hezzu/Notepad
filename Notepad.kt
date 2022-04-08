package main.notepad

import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCombination
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess


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
    private fun openFonts()
    {
        val panel = VBox()
        val secondPane = HBox()
        panel.padding = Insets(5.0)
        panel.style = "-fx-background-color: #474747;"

        val tf = TextField()
        tf.padding = Insets(5.0, 0.0, 0.0, 0.0)
        tf.alignment = Pos.TOP_CENTER
        tf.minWidth = 500.0

        panel.children.addAll(tf, secondPane)

        val scene = Scene(panel, 200.0, 100.0)
        val newWindow = Stage()
        newWindow.title = "Font Selection"
        newWindow.scene = scene

        newWindow.isResizable = false
        newWindow.width = 600.0
        newWindow.height = 800.0
        newWindow.show()
    }

    object Singleton {
        var windowsAmount: Int? = 1
    }
    override fun start(stage: Stage) {
        //Creating a layout, scene and main text Area as it's notepad
        val vBox = VBox()
        val scene = Scene(vBox, 640.0, 480.0)
        val textArea = TextArea()
        textArea.font = Font.font ("Arial", 15.0)

        //Creating a file chooser for later use in save and open
        val fileChooser = FileChooser()
        //Giving a default name to saved files
        fileChooser.initialFileName = "Note.txt"
        //Giving file chooser an extension hint
        val extFilter = FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt")
        fileChooser.extensionFilters.add(extFilter)
        //I fought a lot with this fucker... nullable variable that gives file saved or opened. Its nullable as you can save before picking a file
        var file: File? = null
        val dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")
        val now = LocalDateTime.now()


        //Creating a Menu bar where Menu items will be placed
        val menuBar = MenuBar()

        //Creating file menu items and its subitems
        val fileMenu = Menu("File")

        val newFile = MenuItem("New File")
        newFile.setOnAction {
            file = null
            textArea.text = ""
        }
        newFile.accelerator = KeyCombination.keyCombination("Ctrl+N")

        val newWindow = MenuItem("New Window")
        newWindow.setOnAction {
            start(Stage())
            Singleton.windowsAmount = Singleton.windowsAmount!! + 1
        }
        newWindow.accelerator = KeyCombination.keyCombination("Ctrl+Shift+N")

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

        val saveAs = MenuItem("Save as")
        saveAs.setOnAction {
            try {
                //if that checks value of file and opens a save dialog when it has no valu
                file = fileChooser.showSaveDialog(stage)
                val writer = PrintWriter(file)
                writer.println(textArea.text)
                writer.close()
                stage.title = "${file!!.name} : ${stage.title}"
            }
            catch (ex: IOException) {
                Logger.getLogger(Notepad::class.java.name).log(Level.SEVERE, null, ex)
            }
        }
        saveAs.accelerator = KeyCombination.keyCombination("Ctrl+Shift+S")

        val separator = SeparatorMenuItem()

        val exit = MenuItem("Exit")
        exit.setOnAction {
            if(Singleton.windowsAmount == 1){
                Singleton.windowsAmount = null
                println("Process should be killed")
                exitProcess(0)
            }
            else {
                Singleton.windowsAmount = Singleton.windowsAmount!! - 1
            }
            stage.close()
        }
        //Adding all subitems to menu item
        fileMenu.items.addAll(newFile, newWindow, openFile , saveFile, saveAs, separator, exit)

        //Adding edit menu, its items and subitems
        val editMenu = Menu("Edit")
        val undo = MenuItem("Undo")
        undo.setOnAction {
            textArea.undo()
        }
        undo.accelerator = KeyCombination.keyCombination("CTRL + Z")
        val redo = MenuItem("Redo")
        redo.setOnAction {
            textArea.redo()
        }
        redo.accelerator = KeyCombination.keyCombination("CTRL + Y")
        val separator2 = SeparatorMenuItem()
        val selAll = MenuItem("Select All")
        selAll.setOnAction {
            textArea.selectAll()
        }
        selAll.accelerator = KeyCombination.keyCombination("CTRL + A")
        val copy = MenuItem("Copy")
        copy.setOnAction {
            textArea.copy()
        }
        copy.accelerator = KeyCombination.keyCombination("CTRL + C")
        val paste = MenuItem("Paste")
        paste.setOnAction {
            textArea.paste()
        }
        paste.accelerator = KeyCombination.keyCombination("CTRL + V")
        val cut = MenuItem("Cut")
        cut.setOnAction {
            textArea.cut()
        }
        cut.accelerator = KeyCombination.keyCombination("CTRL + X")
        val del = MenuItem("Delete")
        del.setOnAction {
            textArea.deleteText(textArea.selection)
        }
        del.accelerator = KeyCombination.keyCombination("DELETE")
        val separator3 = SeparatorMenuItem()
        val find = MenuItem("Find") // Todo
        val change = MenuItem("Change") //Todo
        val dAt = MenuItem("Get Time and Date")
        dAt.setOnAction {
            textArea.text += " ${dtf.format(now)}"
        }
        editMenu.items.addAll(undo, redo, separator2,selAll, copy, paste, cut, del, separator3, dAt)
        //Adding format menu, its items and subitems
        val formatMenu = Menu("Format")
        val fonts = MenuItem("Fonts") // Todo
        fonts.setOnAction {
            openFonts()
        }
        formatMenu.items.addAll(fonts)
        //Adding view menu, its items and subitems
        val viewMenu = Menu("View")

        //Adding about menu, its items and subitems
        val aboutMenu = Menu("Debug")

        //Creating a credits' subitem
        val ownerButton = MenuItem("Creator")
        //Action listener that opens new window with my info in it
        ownerButton.setOnAction { openNewWindow("Creator of this Notepad is Hezzu", "Creator") }

        //Creating subitem to view windows size
        val showSizeButton = MenuItem("Show Window size")
        showSizeButton.setOnAction { openNewWindow("Width: ${stage.width}\nHeight: ${stage.height} ", "Parameters") }

        //Creating a Debug Windows amount menu item
        val windowsAmount = MenuItem("Windows Amount")
        windowsAmount.setOnAction {
            openNewWindow("Windows Amount: ${Singleton.windowsAmount}", "Windows Amount")
        }
        //Adding subitems to menu item and then items to menu bar
        aboutMenu.items.addAll(ownerButton, showSizeButton, windowsAmount)
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

        stage.setOnCloseRequest {
            if(Singleton.windowsAmount == 1){
                Singleton.windowsAmount = null
                exitProcess(0)
            }
            else {
                Singleton.windowsAmount = Singleton.windowsAmount!! - 1
            }
        }
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