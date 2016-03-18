COMP1206 Coursework 1 - Fractal Explorer - Joao Diogo Fernandes Conceicao - 27752763

---------------------------------------------------
INSTRUCTIONS FOR USE OF EXTENSIONS AND EXPLANATION
---------------------------------------------------

---------------
 Extension 1:
---------------

Implementation of burning ship, tricorn fractal and Multibrot extra fractals:

The calculatePoints method in each of their respective classes will demonstrate how implementation occured.

----------------
 Extension 2:
----------------

Implementation of arrow keys to move the fractal left, right, up and down:

This works by adjusting the axis values with a fixed value, explained in the code via comments
Please note: This also causes the fractal to zoom out

Implementation of numbers 1-9 on the keyboard as a zoom depth controller using cursor as point to zoom into:

This works by adjusting axis values differently based on which button is pressed.
1 provides the least zoom depth,as you progress from 1 to 9 where 9 is the most zoom depth.
If you click continuously 1,2,3... and don't move the mouse you will see the point zoom in progressively.
In the same idea clicking backwards from 9 to 1 will cause you to zoom out from the point your cursor is in.

Move the cursor to a position on the fractal and clicking one of the numbers and it will zoom in according to its
respective depth.

-----------------
 Extension 3:
-----------------

Implementation of live julia based on the cursor position on the panel.

-----------------
Extension 4:
-----------------

Ability to save image to the same directory as the .java files by providing a name in the name text field.
Clicking save image will store it as a .png file to be able to see later.

