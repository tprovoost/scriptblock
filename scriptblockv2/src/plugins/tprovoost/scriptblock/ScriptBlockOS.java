package plugins.tprovoost.scriptblock;

import icy.plugin.abstract_.Plugin;
import icy.sequence.Sequence;

import java.io.File;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import plugins.adufour.blocks.lang.Block;
import plugins.adufour.blocks.util.VarList;
import plugins.adufour.vars.gui.model.TypeSelectionModel;
import plugins.adufour.vars.gui.model.ValueSelectionModel;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarMutable;
import plugins.adufour.vars.lang.VarString;
import plugins.adufour.vars.util.VarListener;
import plugins.tprovoost.scriptblock.vartransformer.JSScriptBlock;
import plugins.tprovoost.scriptblock.vartransformer.PythonScriptBlock;
import plugins.tprovoost.scripteditor.scriptinghandlers.ScriptingHandler;

public class ScriptBlockOS extends Plugin implements Block
{
    ArrayList<String> languagesInstalled = new ArrayList<String>();

    private VarString scriptType;
    private VarScript inputScript = new VarScript("Script", "output = input0 * 2");

    private VarList inputMap;
    private VarMutable output = new VarMutable("output", Integer.class);

    private int currentIdx = 0;

    public ScriptBlockOS()
    {
        ScriptEngineManager factory = new ScriptEngineManager();
        for (ScriptEngineFactory f : factory.getEngineFactories())
        {
            languagesInstalled.add(f.getLanguageName());
        }
        scriptType = new VarString("Language:", languagesInstalled.get(0));
        scriptType.setDefaultEditorModel(new ValueSelectionModel<String>(languagesInstalled.toArray(new String[0])));
    }

    @Override
    public void run()
    {
        ScriptingHandler handler = inputScript.getEditor().panel.getScriptHandler();
        ScriptEngine engine = handler.createNewEngine();

        for (Var<?> var : inputMap)
        {
            Object value = var.getValue();
            String name = var.getName();
            engine.put(name, value);
        }
        try
        {
            inputScript.evaluate();
        }
        catch (ScriptException e)
        {
            System.out.println(e.getLocalizedMessage());
        }

        Object resObject = engine.get("output");
        String language = inputScript.getEditor().panel.getLanguage();
        if (language.contentEquals("javascript"))
        {
            output.setValue(JSScriptBlock.transformScriptOutput(resObject));
        }
        else if (language.contentEquals("python"))
        {
            output.setValue(PythonScriptBlock.transformScriptOutput(resObject));
        }
        else
        {
            output.setValue(resObject);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void declareInput(VarList inputMap)
    {

        if (this.inputMap == null)
            this.inputMap = inputMap;
        int idx = currentIdx++;

        final VarMutable var;
        if (idx == 0)
        {
            inputMap.add(inputScript);
        }

        var = new VarMutable("input" + idx, Integer.class);
        var.addListener(new VarListener()
        {
            @Override
            public void valueChanged(Var source, Object oldValue, Object newValue)
            {

            }

            @Override
            public void referenceChanged(Var source, Var oldReference, Var newReference)
            {
                if (newReference != null)
                {
                    // add a new variable recursively
                    declareInput(ScriptBlockOS.this.inputMap);
                }
                else if (ScriptBlockOS.this.inputMap.size() > 1)
                {
                    ScriptBlockOS.this.inputMap.remove(var);
                }
            }
        });
        var.setDefaultEditorModel(new TypeSelectionModel(new Class<?>[] {null, Sequence.class, Integer.class,
                Double.class, int[].class, double[].class, String.class, File.class, File[].class, Object[].class}));
        inputMap.add(var.getName(), var);
    }

    @Override
    public void declareOutput(VarList outputMap)
    {
        output.setDefaultEditorModel(new TypeSelectionModel(new Class<?>[] {null, Sequence.class, Integer.class,
                Double.class, int[].class, double[].class, String.class, File.class, File[].class, Object[].class}));
        outputMap.add(output);

        // if (this.output == null)
        // this.outputMap = outputMap;
        // int idx = currentIdxO++;
        // final VarMutable var = new VarMutable("output" + idx, Integer.class);
        // var.addListener(new VarListener()
        // {
        // @Override
        // public void valueChanged(Var source, Object oldValue, Object newValue)
        // {
        //
        // }
        //
        // @Override
        // public void referenceChanged(Var source, Var oldReference, Var newReference)
        // {
        // if (newReference != null)
        // {
        // // add a new variable recursively
        // declareOutput(ScriptBlockOS.this.outputMap);
        // }
        // else if (ScriptBlockOS.this.outputMap.size() > 1)
        // {
        // ScriptBlockOS.this.outputMap.remove(var);
        // }
        // }
        // });

    }

}
