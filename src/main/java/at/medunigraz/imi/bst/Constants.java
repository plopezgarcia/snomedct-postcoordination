package at.medunigraz.imi.bst;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Constants {

    public enum SUBHIERARCHY {  BS, CF, EG, EV, OE, OR, PB, PF, PO,
                                PR, QV, RA, SI, SO, SP, SN, ST, SU}

    public static final Map<SUBHIERARCHY, Integer> SUBHIERARCHY_MAPPINGS;
    static {
        Map<SUBHIERARCHY, Integer> theMap = new HashMap<SUBHIERARCHY, Integer>();
        theMap.put(SUBHIERARCHY.BS, 23037004);
        theMap.put(SUBHIERARCHY.CF, 404684003);
        theMap.put(SUBHIERARCHY.EG, 308916002);
        theMap.put(SUBHIERARCHY.EV, 272379006);
        theMap.put(SUBHIERARCHY.OE, 363787002);
        theMap.put(SUBHIERARCHY.OR, 410607006);
        theMap.put(SUBHIERARCHY.PB, 373873005);
        theMap.put(SUBHIERARCHY.PF, 78621006);
        theMap.put(SUBHIERARCHY.PO, 260787004);
        theMap.put(SUBHIERARCHY.PR, 71388002);
        theMap.put(SUBHIERARCHY.QV, 362981000);
        theMap.put(SUBHIERARCHY.RA, 419891008);
        theMap.put(SUBHIERARCHY.SI, 243796009);
        theMap.put(SUBHIERARCHY.SO, 48176007);
        theMap.put(SUBHIERARCHY.SP, 370115009);
        theMap.put(SUBHIERARCHY.SN, 123038009);
        theMap.put(SUBHIERARCHY.ST, 254291000);
        theMap.put(SUBHIERARCHY.SU, 105590001);
        SUBHIERARCHY_MAPPINGS = Collections.unmodifiableMap(theMap);
    }

}