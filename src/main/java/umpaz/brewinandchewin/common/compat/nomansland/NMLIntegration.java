package umpaz.brewinandchewin.common.compat.nomansland;

import net.minecraft.resources.Identifier;
import umpaz.brewinandchewin.BrewinAndChewin;

public class NMLIntegration {
    public static final String MOD_ID = "nomansland";

    public static final Identifier WALNUTS = Identifier.fromNamespaceAndPath(MOD_ID, "walnuts");
    public static final Identifier MAPLE_SYRUP_BOTTLE = Identifier.fromNamespaceAndPath(MOD_ID, "maple_syrup_bottle");
    public static final Identifier THISTLE = Identifier.fromNamespaceAndPath(MOD_ID, "thistle");

    public static final Identifier PRAIRIE = Identifier.fromNamespaceAndPath(MOD_ID, "prairie");

    public static final Identifier RICE_PUDDING_ITEM = BrewinAndChewin.asResource("rice_pudding");
    public static final Identifier MAPLE_FUDGE_ITEM = BrewinAndChewin.asResource("maple_fudge");

    public static boolean isLoaded() {
        return BrewinAndChewin.getHelper().isModLoadedEarly(MOD_ID);
    }
}
