package com.example.administrator.thinker_soft.niftydialogeffects;

import com.example.administrator.thinker_soft.niftydialogeffects.effects.BaseEffects;
import com.example.administrator.thinker_soft.niftydialogeffects.effects.FadeIn;
import com.example.administrator.thinker_soft.niftydialogeffects.effects.FlipH;
import com.example.administrator.thinker_soft.niftydialogeffects.effects.FlipV;
import com.example.administrator.thinker_soft.niftydialogeffects.effects.NewsPaper;
import com.example.administrator.thinker_soft.niftydialogeffects.effects.SideFall;
import com.example.administrator.thinker_soft.niftydialogeffects.effects.SlideLeft;
import com.example.administrator.thinker_soft.niftydialogeffects.effects.SlideRight;
import com.example.administrator.thinker_soft.niftydialogeffects.effects.SlideTop;

/**
 * Created by lee on 2014/7/30.
 */
public enum  Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(com.example.administrator.thinker_soft.niftydialogeffects.effects.SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(com.example.administrator.thinker_soft.niftydialogeffects.effects.Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(com.example.administrator.thinker_soft.niftydialogeffects.effects.RotateBottom.class),
    RotateLeft(com.example.administrator.thinker_soft.niftydialogeffects.effects.RotateLeft.class),
    Slit(com.example.administrator.thinker_soft.niftydialogeffects.effects.Slit.class),
    Shake(com.example.administrator.thinker_soft.niftydialogeffects.effects.Shake.class),
    Sidefill(SideFall.class);
    private Class effectsClazz;

    private Effectstype(Class mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        try {
            return (BaseEffects) effectsClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
