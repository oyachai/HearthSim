/* Graphics class supporting EPS export from plots.

 Copyright (c) 1998-2010 The Regents of the University of California.
 All rights reserved.
 Permission is hereby granted, without written agreement and without
 license or royalty fees, to use, copy, modify, and distribute this
 software and its documentation for any purpose, provided that the above
 copyright notice and the following two paragraphs appear in all copies
 of this software.

 IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
 FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
 ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
 THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
 PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
 CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 ENHANCEMENTS, OR MODIFICATIONS.

 PT_COPYRIGHT_VERSION_2
 COPYRIGHTENDKEY
 */
package com.ptplot;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.ImageObserver;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;


///////////////////////////////////////////////////////////////////
//// EPSGraphics

/**
 Graphics class supporting EPS export from plots.
 If this is used from within an applet, then the output goes to the standard
 output.  Unfortunately, with standard browsers, this is not useful.
 With MS Internet Explorer, standard output is not available.
 With Netscape Navigator, standard output is available in the Java console,
 but is limited to fewer lines than what is usually generated.
 Thus, we recommend using this within Sun's appletviewer, and redirecting
 its standard output to a file.

 @author Edward A. Lee
 @version $Id: EPSGraphics.java 57040 2010-01-27 20:52:32Z cxh $
 @since Ptolemy II 0.2
 @Pt.ProposedRating Yellow (cxh)
 @Pt.AcceptedRating Yellow (cxh)
 */
@SuppressWarnings("PMD")
public class EPSGraphics extends Graphics {
    /** Constructor for a graphics object that writes encapsulated
     *  PostScript to the specified output stream.  If the out argument is
     *  null, then it writes to standard output (it would write it to
     *  the clipboard, but as of this writing, writing to the clipboard
     *  does not work in Java).
     *  @param out The stream to write to, or null to write to standard out.
     *  @param width The width of the plot graphic, in units of 1/72 inch.
     *  @param height The height of the plot graphic, in units of 1/72 inch.
     */
    public EPSGraphics(OutputStream out, int width, int height) {
        _width = width;
        _height = height;
        _out = out;
        _buffer.append("%!PS-Adobe-3.0 EPSF-3.0\n");
        _buffer.append("%%Creator: UC Berkeley Plot Package\n");
        _buffer.append("%%BoundingBox: 50 50 " + (50 + width) + " "
                + (50 + height) + "\n");
        _buffer.append("%%Pages: 1\n");
        _buffer.append("%%Page: 1 1\n");
        _buffer.append("%%LanguageLevel: 2\n");
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////
    @Override
    public void clearRect(int x, int y, int width, int height) {
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
    }

    @Override
    public Graphics create() {
        return new EPSGraphics(_out, _width, _height);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle,
            int arcAngle) {
    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return true;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height,
            ImageObserver observer) {
        return true;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor,
            ImageObserver observer) {
        return true;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height,
            Color bgcolor, ImageObserver observer) {
        return true;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
            int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        return true;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
            int sx1, int sy1, int sx2, int sy2, Color bgcolor,
            ImageObserver observer) {
        return true;
    }

    /** Draw a line, using the current color, between the points (x1, y1)
     *  and (x2, y2) in this graphics context's coordinate system.
     *  @param x1 the x coordinate of the first point.
     *  @param y1 the y coordinate of the first point.
     *  @param x2 the x coordinate of the second point.
     *  @param y2 the y coordinate of the second point.
     */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        Point start = _convert(x1, y1);
        Point end = _convert(x2, y2);
        _buffer.append("newpath " + start.x + " " + start.y + " moveto\n");
        _buffer.append("" + end.x + " " + end.y + " lineto\n");
        _buffer.append("stroke\n");
    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
    }

    /** Draw a closed polygon defined by arrays of x and y coordinates.
     *  Each pair of (x, y) coordinates defines a vertex. The third argument
     *  gives the number of vertices.  If the arrays are not long enough to
     *  define this many vertices, or if the third argument is less than three,
     *  then nothing is drawn.
     *  @param xPoints An array of x coordinates.
     *  @param yPoints An array of y coordinates.
     *  @param nPoints The total number of vertices.
     */
    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        if (!_polygon(xPoints, yPoints, nPoints)) {
            return;
        } else {
            _buffer.append("closepath stroke\n");
        }
    }

    /** Draw an oval bounded by the specified rectangle with the current color.
     *  @param x The x coordinate of the upper left corner
     *  @param y The y coordinate of the upper left corner
     *  @param width The width of the oval to be filled.
     *  @param height The height of the oval to be filled.
     */

    // FIXME: Currently, this ignores the fourth argument and draws a circle
    // with diameter given by the third argument.
    @Override
    public void drawOval(int x, int y, int width, int height) {
        int radius = width / 2;
        Point center = _convert(x + radius, y + radius);
        _buffer.append("newpath " + center.x + " " + center.y + " " + radius
                + " 0 360 arc closepath stroke\n");
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {
        Point start = _convert(x, y);
        _buffer.append("newpath " + start.x + " " + start.y + " moveto\n");
        _buffer.append("0 " + (-height) + " rlineto\n");
        _buffer.append("" + width + " 0 rlineto\n");
        _buffer.append("0 " + height + " rlineto\n");
        _buffer.append("" + (-width) + " 0 rlineto\n");
        _buffer.append("closepath stroke\n");
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height,
            int arcWidth, int arcHeight) {
    }

    @Override
    public void drawString(java.text.AttributedCharacterIterator iterator,
            int x, int y) {
        // FIXME: This method is present in the graphics class in JDK1.2,
        // but not in JDK1.1.
        throw new RuntimeException(
                "Sorry, drawString(java.text.AttributedCharacterIterator, "
                        + "int , int) is not implemented in EPSGraphics");
    }

    /** Draw a string.  "(" is converted to "\(" and
     *  ")" is converted to "\) so as to avoid EPS Syntax errors.
     *  @param str The string to draw.
     *  @param x  The x location of the string.
     *  @param y  The y location of the string.
     */
    @Override
    public void drawString(String str, int x, int y) {
        Point start = _convert(x, y);
        _buffer.append("" + start.x + " " + start.y + " moveto\n");

        if ((str.indexOf("(") > -1) && (str.indexOf("\\(") == -1)) {
            str = StringUtilities.substitute(str, "(", "\\(");
        }

        if ((str.indexOf(")") > -1) && (str.indexOf("\\)") == -1)) {
            str = StringUtilities.substitute(str, ")", "\\)");
        }

        _buffer.append("(" + str + ") show\n");
    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle,
            int arcAngle) {
    }

    /** Draw a filled polygon defined by arrays of x and y coordinates.
     *  Each pair of (x, y) coordinates defines a vertex. The third argument
     *  gives the number of vertices.  If the arrays are not long enough to
     *  define this many vertices, or if the third argument is less than three,
     *  then nothing is drawn.
     *  @param xPoints An array of x coordinates.
     *  @param yPoints An array of y coordinates.
     *  @param nPoints The total number of vertices.
     */
    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        if (!_polygon(xPoints, yPoints, nPoints)) {
            return;
        } else {
            _buffer.append("closepath fill\n");
        }
    }

    /** Fill an oval bounded by the specified rectangle with the current color.
     *  @param x The x coordinate of the upper left corner
     *  @param y The y coordinate of the upper left corner
     *  @param width The width of the oval to be filled.
     *  @param height The height of the oval to be filled.
     */

    // FIXME: Currently, this ignores the fourth argument and draws a circle
    // with diameter given by the third argument.
    @Override
    public void fillOval(int x, int y, int width, int height) {
        int radius = width / 2;
        Point center = _convert(x + radius, y + radius);
        _buffer.append("newpath " + center.x + " " + center.y + " " + radius
                + " 0 360 arc closepath fill\n");
    }

    /** Fill the specified rectangle and draw a thin outline around it.
     *  The left and right edges of the rectangle are at x and x + width - 1.
     *  The top and bottom edges are at y and y + height - 1.
     *  The resulting rectangle covers an area width pixels wide by
     *  height pixels tall. The rectangle is filled using the
     *  brightness of the current color to set the level of gray.
     *  @param x The x coordinate of the top left corner.
     *  @param y The y coordinate of the top left corner.
     *  @param width The width of the rectangle.
     *  @param height The height of the rectangle.
     */
    @Override
    public void fillRect(int x, int y, int width, int height) {
        Point start = _convert(x, y);
        _fillPattern();
        _buffer.append("newpath " + start.x + " " + start.y + " moveto\n");
        _buffer.append("0 " + (-height) + " rlineto\n");
        _buffer.append("" + width + " 0 rlineto\n");
        _buffer.append("0 " + height + " rlineto\n");
        _buffer.append("" + (-width) + " 0 rlineto\n");
        _buffer.append("closepath gsave fill grestore\n");
        _buffer.append("0.5 setlinewidth 0 setgray [] 0 setdash stroke\n");

        // reset the gray scale to black
        _buffer.append("1 setlinewidth\n");
    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height,
            int arcWidth, int arcHeight) {
    }

    @Override
    public Shape getClip() {
        return null;
    }

    @Override
    public Rectangle getClipBounds() {
        return null;
    }

    @Override
    public Color getColor() {
        return _currentColor;
    }

    @Override
    public Font getFont() {
        return _currentFont;
    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
        return null;
    }

    @Override
    public void setFont(Font font) {
        if (font == null) {
            return;
        }

        int size = font.getSize();
        boolean bold = font.isBold();

        if (bold) {
            _buffer.append("/Helvetica-Bold findfont\n");
        } else {
            _buffer.append("/Helvetica findfont\n");
        }

        _buffer.append("" + size + " scalefont setfont\n");
        _currentFont = font;
    }

    @Override
    public void setClip(Shape clip) {
    }

    @Override
    public void setClip(int x, int y, int width, int height) {
    }

    /** Set the current color.  Since we are generating gray scale
     *  postscript, set a line style. Set the gray level to zero (black).
     *  @param c The desired current color.
     */
    @Override
    public void setColor(Color c) {
        if (c == Color.black) {
            _buffer.append("0 setgray\n");
            _buffer.append("[] 0 setdash\n");
            _buffer.append("1 setlinewidth\n");
        } else if (c == Color.white) {
            _buffer.append("1 setgray\n");
            _buffer.append("[] 0 setdash\n");
            _buffer.append("1 setlinewidth\n");
        } else if (c == Color.lightGray) {
            _buffer.append("0.9 setgray\n");
            _buffer.append("[] 0 setdash\n");
            _buffer.append("0.5 setlinewidth\n");
        } else {
            if (_linepattern.containsKey(c)) {
                _buffer.append(_linepattern.get(c) + " 0 setdash\n");
                _buffer.append("1 setlinewidth\n");
            } else {
                _buffer.append("0 setgray\n");

                // construct a new line pattern.
                if (_patternIndex >= _patterns.length) {
                    _patternIndex = 0;
                }

                _buffer.append(_patterns[_patternIndex] + " 0 setdash\n");
                _buffer.append("1 setlinewidth\n");
                _linepattern.put(c, _patterns[_patternIndex]);
                _patternIndex++;
            }
        }

        _currentColor = c;
    }

    @Override
    public void setPaintMode() {
    }

    @Override
    public void setXORMode(Color c1) {
    }

    /** Issue the PostScript showpage command, then write and flush the output.
     *  If the output argument of the constructor was null, then write
     *  to the clipboard.
     */
    public void showpage() {
        _buffer.append("showpage\n");

        if (_out != null) {
            PrintWriter output = new PrintWriter(new BufferedOutputStream(_out));

            output.println(_buffer.toString());
            output.flush();
        } else {
            // Write to clipboard instead
            // NOTE: This doesn't work at least with jdk 1.3beta
            if (_clipboard == null) {
                _clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            }

            StringSelection sel = new StringSelection(_buffer.toString());
            _clipboard.setContents(sel, sel);
        }
    }

    @Override
    public void translate(int x, int y) {
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private methods                   ////
    // Convert the screen coordinate system to that of postscript.
    private Point _convert(int x, int y) {
        return new Point(x + 50, (_height + 50) - y);
    }

    // Draw a closed polygon defined by arrays of x and y coordinates.
    // Return false if arguments are misformed.
    private boolean _polygon(int[] xPoints, int[] yPoints, int nPoints) {
        if ((nPoints < 3) || (xPoints.length < nPoints)
                || (yPoints.length < nPoints)) {
            return false;
        }

        Point start = _convert(xPoints[0], yPoints[0]);
        _buffer.append("newpath " + start.x + " " + start.y + " moveto\n");

        for (int i = 1; i < nPoints; i++) {
            Point vertex = _convert(xPoints[i], yPoints[i]);
            _buffer.append("" + vertex.x + " " + vertex.y + " lineto\n");
        }

        return true;
    }

    // Issue a command to set the fill pattern.
    // Currently, this is a gray level that is a function of the color.
    private void _fillPattern() {
        // FIXME: We probably want a fill pattern rather than
        // just a gray scale.
        int red = _currentColor.getRed();
        int green = _currentColor.getGreen();
        int blue = _currentColor.getBlue();

        // Scaling constants so that fully saturated R, G, or B appear
        // different.
        double bluescale = 0.6; // darkest
        double redscale = 0.8;
        double greenscale = 1.0; // lightest
        double fullscale = Math.sqrt(255.0 * 255.0 * ((bluescale * bluescale)
                + (redscale * redscale) + (greenscale * greenscale)));
        double graylevel = Math.sqrt(((red * red * redscale * redscale)
                + (blue * blue * bluescale * bluescale) + (green * green
                * greenscale * greenscale)))
                / fullscale;
        _buffer.append("" + graylevel + " setgray\n");

        // NOTE -- for debugging, output color spec in comments
        _buffer.append("%---- rgb: " + red + " " + green + " " + blue + "\n");
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////
    private Color _currentColor = Color.black;

    private Font _currentFont;

    private int _width;

    private int _height;

    private Hashtable<Color, String> _linepattern = new Hashtable<Color, String>();

    private OutputStream _out;

    private StringBuffer _buffer = new StringBuffer();

    private Clipboard _clipboard;

    // Default line patterns.
    // FIXME: Need at least 11 of these.
    static private String[] _patterns = { "[]", "[1 1]", "[4 4]", "[4 4 1 4]",
            "[2 2]", "[4 2 1 2 1 2]", "[5 3 2 3]", "[3 3]", "[4 2 1 2 2 2]",
            "[1 2 5 2 1 2 1 2]", "[4 1 2 1]", };

    private int _patternIndex = 0;
}
