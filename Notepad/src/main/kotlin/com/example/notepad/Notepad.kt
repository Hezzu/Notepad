package com.example.notepad

import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.stage.Stage

class HelloApplication : Application() {

    private fun openFile(stage: Stage)
    {
        val fileChooser = FileChooser();
        fileChooser.title = "Select file to open"
        fileChooser.showOpenDialog(stage)
    }
//    private fun saveFile(stage: Stage)
//    {
//
//        val fileChooser = FileChooser();
//        fileChooser.showSaveDialog(stage)
//    }
    private fun openNewWindow(infoText: String, title: String)
    {
        val info = Label(infoText)
        info.padding = Insets(5.0)
        info.alignment = Pos.CENTER
        val scene = Scene(info, 200.0, 100.0)
        val newWindow = Stage()
        newWindow.title = title
        newWindow.scene = scene
        newWindow.isResizable = false
        newWindow.width = 300.0
        newWindow.height = 200.0
        newWindow.show()
    }
    override fun start(stage: Stage) {
        val vBox = VBox()
        val scene = Scene(vBox, 640.0, 480.0)

        val menuBar = MenuBar()
        val fileMenu = Menu("File")
        val openFile = MenuItem("Open a File")
        fileMenu.items.add(openFile)
        openFile.setOnAction { openFile(stage) }

        val editMenu = Menu("Edit")
        val formatMenu = Menu("Format")
        val viewMenu = Menu("View")

        val aboutMenu = Menu("About")
        val ownerButton = MenuItem("Creator")
        ownerButton.setOnAction { openNewWindow("Creator of this Notepad is Hezzu", "Creator") }
        val showSizeButton = MenuItem("Show Window size")
        showSizeButton.setOnAction { openNewWindow("Width: ${stage.width}\nHeight: ${stage.height} ", "Parameters") }
        aboutMenu.items.addAll(ownerButton, showSizeButton)

        menuBar.menus.addAll(fileMenu, editMenu, formatMenu, viewMenu, aboutMenu)

        val textArea = TextArea()
        vBox.children.addAll(menuBar, textArea)
        textArea.maxHeight = 1920.0
        textArea.prefHeight = 1920.0
        vBox.maxHeight = 1920.0
        vBox.padding = Insets(5.0, 5.0, 5.0, 5.0)
        stage.minHeight = 120.0
        stage.minWidth = 270.0
        menuBar.border = Border(BorderStroke(
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
            BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
            CornerRadii(5.0, 5.0, 0.0, 0.0, false), BorderWidths(2.0, 2.0, 1.0, 2.0), Insets.EMPTY))

        menuBar.style = """
            -fx-background-radius: 5;
            -fx-background-color: grey;
        """.trimIndent()
        textArea.style = """
            -fx-background-radius: 5;
        """.trimIndent()
        vBox.style = "-fx-background-color: #474747;"

        textArea.border = Border(BorderStroke(
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
            BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
            CornerRadii(0.0, 0.0, 5.0, 5.0, false), BorderWidths(1.0, 2.0, 2.0, 2.0), Insets.EMPTY))


        stage.title = "Crappy Notepad!"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}