# java_ryisnow_notepad
I create a notepad by following RyiSnow Tutorial

# About the study

  I follow video tutorial to create Java application.

  It include below function:

  - New file
  - Open file
  - Save
  - Save As
  - Undo
  - Redo
  - World Wrap
  - Set Font and Font Size
  - Change Color

  It is a simple app but helps me study some common Java UI component such as `JTextArea`, `JMenuBar`, `JScrollPane`, `KeyListener`, `UndoManager`, `JMenu`, `JMenuItem` and etc.

  I also added some additional fonts into my notepad such `Fira Code` and `Noto Sans`.

# Issue

  1. Cannot support `Keybroad Input Method`

     JTextArea can display chinese and emoji by selecting Font `Noto Sans TC Thin` (which I downloaded from Google and import in my app's resources)

     But I found out JTextArea cannot input Chinese Character(e.g. 中文字) and Emoji (e.g. 😎😍💪🏼)

     Say you type chinese or japanese using `keybroad input method`, it doesn't work.

     I search internet and it said the reason behind is that `Keybroad Input Method` is Window's function while java is running inside JVM.

     We do not have simple solution for JVM to receive Window `Keybroad Input Method`

     I don't know.

  3. Cannot support font family

     For example VS Code is using font family `Consolas, 'Courier New', monospace`

     It contains more than one fonts so that when one of the font cannot display a particular character it can use the other fonts.

     But JTextArea can only support one font at a time.

     which mean when you have mulitple languages in your text or emoji it may only be able to display some of it since the font you used may not cover all characters

     I also try JTextPane but it seems there is no simple solution to solve this issue

# Reference

  - RyiSnow Java Notepad Tutorial

    https://www.youtube.com/watch?v=UZyZ31nrL2U&list=PL_QPQmz5C6WUTPONMeQcEEdKax0wGsnZB&index=1
