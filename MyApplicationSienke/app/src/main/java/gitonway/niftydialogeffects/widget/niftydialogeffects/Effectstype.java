package gitonway.niftydialogeffects.widget.niftydialogeffects;

import gitonway.niftydialogeffects.widget.niftydialogeffects.effects.BaseEffects;
import gitonway.niftydialogeffects.widget.niftydialogeffects.effects.FadeIn;
import gitonway.niftydialogeffects.widget.niftydialogeffects.effects.FlipH;
import gitonway.niftydialogeffects.widget.niftydialogeffects.effects.FlipV;
import gitonway.niftydialogeffects.widget.niftydialogeffects.effects.NewsPaper;
import gitonway.niftydialogeffects.widget.niftydialogeffects.effects.SideFall;
import gitonway.niftydialogeffects.widget.niftydialogeffects.effects.SlideLeft;
import gitonway.niftydialogeffects.widget.niftydialogeffects.effects.SlideRight;
import gitonway.niftydialogeffects.widget.niftydialogeffects.effects.SlideTop;

/**
 * Created by lee on 2014/7/30.
 */
public enum  Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(gitonway.niftydialogeffects.widget.niftydialogeffects.effects.SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(gitonway.niftydialogeffects.widget.niftydialogeffects.effects.Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(gitonway.niftydialogeffects.widget.niftydialogeffects.effects.RotateBottom.class),
    RotateLeft(gitonway.niftydialogeffects.widget.niftydialogeffects.effects.RotateLeft.class),
    Slit(gitonway.niftydialogeffects.widget.niftydialogeffects.effects.Slit.class),
    Shake(gitonway.niftydialogeffects.widget.niftydialogeffects.effects.Shake.class),
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
