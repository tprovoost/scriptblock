package plugins.tprovoost.scriptblock;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.lang.VarString;
import plugins.tprovoost.scripteditor.scriptinghandlers.ScriptingHandler;

public class VarScript extends VarString
{
    private VarScriptEditor editor;

    public VarScript(String name, String defaultValue)
    {
        super(name, defaultValue);
        setEditor(new VarScriptEditor(this, defaultValue));
    }

    @Override
    public String getValue()
    {
        if (getEditor() != null)
            return getEditor().getText();
        return getDefaultValue();
    }

    @Override
    public void setValue(String newValue) throws IllegalAccessError
    {
        getEditor().setText(newValue);
    }

    @Override
    public VarEditor<String> createVarEditor()
    {
        return getEditor();
    }

    public void evaluate() throws ScriptException
    {
        ScriptingHandler handler = getEditor().panel.getScriptHandler();
        ScriptEngine engine = handler.getEngine();
        handler.eval(engine, getEditor().panel.getTextArea().getText());
    }

    public ScriptEngine getEngine()
    {
        return getEditor().panel.getScriptHandler().getEngine();
    }

    public VarScriptEditor getEditor()
    {
        return editor;
    }

    public void setEditor(VarScriptEditor editor)
    {
        this.editor = editor;
    }
}
