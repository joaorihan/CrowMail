package com.joao.crowmail.crows;

import com.joao.crowmail.CrowMail;
import com.joao.crowmail.letters.Letter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CrowManager {

    private CrowMail plugin;


    public CrowManager(CrowMail plugin) {
        setPlugin(plugin);
    }


    public void removeAllCrows(){
        for (Letter outgoingLetter : getPlugin().getOutgoingManager().getOutgoingLetters())
            if (outgoingLetter.getCrow().getCrowEntity().isValid())
                outgoingLetter.getCrow().remove();
    }

}
