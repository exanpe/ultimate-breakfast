/**
 * 
 */
package fr.ultimate.breakfast.web.constants;

/**
 * @author lguerin
 */
public enum JsConsoleCommandEnum
{
    HELP, LOGIN, LOGOUT, WHOAMI, LS, ADDUSER, TOPUSER, DELUSER, RENAMEUSER, POSITION, SENDMAIL, VALIDATE;

    public String toString()
    {
        return super.toString().toLowerCase();
    };

    public static JsConsoleCommandEnum fromCommand(String command)
    {
        for (JsConsoleCommandEnum elem : JsConsoleCommandEnum.values())
        {
            if (elem.name().equalsIgnoreCase(command)) { return elem; }
        }
        return null;
    }

}
