package plugins.tprovoost.scriptblock.vartransformer;

import icy.plugin.PluginLoader;
import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.ImporterTopLevel;

public class JSScriptBlock
{

    public static Object transformScriptOutput(Object o)
    {
        return Context.jsToJava(o, Object.class);
    }

    public static Object transformInputForScript(Object o)
    {
        Object toReturn = o;

        Context cx = Context.enter();
        cx.setApplicationClassLoader(PluginLoader.getLoader());
        toReturn = Context.javaToJS(toReturn, new ImporterTopLevel(cx));
        return toReturn;
    }

}
