package myapp.presentationmodel.canton;

/**
 * @author Dieter Holz
 *
 * todo: specify all commands you need in your corresponding controller
 */
public interface CantonCommands {
	String LOAD_SOME_CANTON = unique("loadSomeCanton");
	String SAVE             = unique("save");
	String RESET            = unique("reset");

	static String unique(String key) {
		return CantonCommands.class.getName() + "." + key;
	}

}
