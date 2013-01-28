package plugins.tprovoost.scriptblock.vartransformer;

import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.NativeArray;

public class JSScriptBlock
{

    public static Object transformScriptOutput(Object o)
    {
        if (o instanceof NativeArray)
        {
            return Context.jsToJava(o, Object[].class);
        }
        else
            return o;
    }

}
