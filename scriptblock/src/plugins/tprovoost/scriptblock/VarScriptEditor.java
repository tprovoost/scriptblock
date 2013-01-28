package plugins.tprovoost.scriptblock;

import icy.gui.frame.IcyFrame;
import icy.gui.frame.IcyFrameAdapter;
import icy.gui.frame.IcyFrameEvent;
import icy.gui.frame.IcyFrameListener;
import icy.plugin.PluginLoader;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.WindowConstants;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import plugins.adufour.vars.gui.swing.SwingVarEditor;
import plugins.tprovoost.scripteditor.gui.ScriptingPanel;

public class VarScriptEditor extends SwingVarEditor<String>
{

    ScriptingPanel panel;
    private IcyFrame frame;
    private RSyntaxTextArea textArea;
    private MouseListener mouseListener;
    private IcyFrameListener frameListener;

    public VarScriptEditor(VarScript varScript, String defaultValue)
    {
        super(varScript);
        panel = new ScriptingPanel(null, "Untitled", "javascript", true);
        panel.setText(defaultValue);

        frame = new IcyFrame("Script Block Editor", true, true, true, true);
        frame.setContentPane(panel);
        frame.setSize(300, 300);
        frame.setVisible(true);
        frame.addToMainDesktopPane();
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        mouseListener = new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    frame.setVisible(true);
                    frame.requestFocus();
                    e.consume();
                }
            }
        };

        frameListener = new IcyFrameAdapter()
        {
            @Override
            public void icyFrameClosing(IcyFrameEvent e)
            {
                textArea.setText(panel.getTextArea().getText());
                textArea.repaint();
                String language = panel.getLanguage();
                if (language.contentEquals("javascript"))
                {
                    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
                }
                else if (language.contentEquals("python"))
                {
                    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
                }
                else
                {
                    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
                }
            }
        };

    }

    @Override
    protected JComponent createEditorComponent()
    {
        textArea = new RSyntaxTextArea(10, 30);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        Theme t;
        try
        {
            t = Theme.load(PluginLoader.getLoader().getResourceAsStream(
                    "plugins/tprovoost/scripteditor/resources/themes/eclipse.xml"));
            t.apply(textArea);
        }
        catch (IOException e)
        {
        }
        textArea.setCodeFoldingEnabled(true);
        textArea.setAntiAliasingEnabled(true);
        textArea.setAutoIndentEnabled(true);
        textArea.setCloseCurlyBraces(true);
        textArea.setMarkOccurrences(true);
        textArea.setCodeFoldingEnabled(true);
        textArea.setPaintMarkOccurrencesBorder(true);
        textArea.setPaintMatchedBracketPair(true);
        textArea.setPaintTabLines(true);
        textArea.setEditable(false);
        textArea.setText(getText());

        RTextScrollPane pane = new RTextScrollPane(textArea);
        pane.setIconRowHeaderEnabled(true);
        pane.setLineNumbersEnabled(false);

        return pane;
    }

    @Override
    protected void activateListeners()
    {
        frame.addFrameListener(frameListener);
        textArea.addMouseListener(mouseListener);
    }

    @Override
    protected void deactivateListeners()
    {
        textArea.removeMouseListener(mouseListener);
        frame.removeFrameListener(frameListener);
        frame.close();
    }

    @Override
    protected void updateInterfaceValue()
    {

    }

    public String getText()
    {
        if (panel == null || panel.getTextArea() == null)
            return getVariable().getDefaultValue();
        return panel.getTextArea().getText();
    }

    public void setText(String newValue)
    {
        panel.getTextArea().setText(newValue);
    }

}
