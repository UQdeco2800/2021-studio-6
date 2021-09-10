import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class MyPacker {
    public static void main (String[] args) throws Exception {
        TexturePacker.process("core/assets/images/playeritems",
                "core/assets/images/Player_Spritesheets",
                "PlayerItems");
    }
}
