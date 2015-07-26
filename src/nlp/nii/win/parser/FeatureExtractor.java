/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser;

import nlp.nii.win.parser.element.DPState;
import nlp.nii.win.parser.element.Feature;
import nlp.nii.win.parser.element.Features;

/**
 *
 * @author lelightwin
 */
public class FeatureExtractor {

    public static Feature[] baselineFeaturesFrom(DPState p) {
        Feature[] feats = new Feature[BaselineFeature.quantity];
        int s0c = p.s0c(), s0t = p.s0t(), s0w = p.s0w(), s1c = p.s1c(), s1t = p.s1t(), s1w = p.s1w();
        int s2c = p.s2c(), s2t = p.s2t(), s2w = p.s2w(), s3c = p.s3c(), s3t = p.s3t(), s3w = p.s3w();
        int q0t = p.q0t(), q1t = p.q1t(), q2t = p.q2t(), q3t = p.q3t();
        int q0w = p.q0w(), q1w = p.q1w(), q2w = p.q2w(), q3w = p.q3w();
        int s0lc = p.s0lc(), s0rc = p.s0rc(), s0uc = p.s0uc(), s0lw = p.s0lw(), s0rw = p.s0rw(), s0uw = p.s0uw();
        int s1lc = p.s1lc(), s1rc = p.s1rc(), s1uc = p.s1uc(), s1lw = p.s1lw(), s1rw = p.s1rw(), s1uw = p.s1uw();

        feats[BaselineFeature.s0c_s0t] = Features.from(s0c, s0t);
        feats[BaselineFeature.s0c_s0w] = Features.from(s0c, s0w);

        feats[BaselineFeature.s1c_s1t] = Features.from(s1c, s1t);
        feats[BaselineFeature.s1c_s1w] = Features.from(s1c, s1w);

        feats[BaselineFeature.s2c_s2t] = Features.from(s2c, s2t);
        feats[BaselineFeature.s2c_s2w] = Features.from(s2c, s2w);

        feats[BaselineFeature.s3c_s3t] = Features.from(s3c, s3t);
        feats[BaselineFeature.s3c_s3w] = Features.from(s3c, s3w);

        feats[BaselineFeature.q0t_q0w] = Features.from(q0t, q0w);
        feats[BaselineFeature.q1t_q1w] = Features.from(q1t, q1w);
        feats[BaselineFeature.q2t_q2w] = Features.from(q2t, q2w);
        feats[BaselineFeature.q3t_q3w] = Features.from(q3t, q3w);

        feats[BaselineFeature.s0lc_s0lw] = Features.from(s0lc, s0lw);
        feats[BaselineFeature.s0rc_s0rw] = Features.from(s0rc, s0rw);
        feats[BaselineFeature.s0uc_s0uw] = Features.from(s0uc, s0uw);

        feats[BaselineFeature.s1lc_s1lw] = Features.from(s1lc, s1lw);
        feats[BaselineFeature.s1rc_s1rw] = Features.from(s1rc, s1rw);
        feats[BaselineFeature.s1uc_s1uw] = Features.from(s1uc, s1uw);

        // bigram features
        feats[BaselineFeature.s1w_s0w] = Features.from(s1w, s0w);
        feats[BaselineFeature.s1w_s0c] = Features.from(s0c, s1w);
        feats[BaselineFeature.s1c_s0w] = Features.from(s1c, s0w);
        feats[BaselineFeature.s1c_s0c] = Features.from(s1c, s0c);

        feats[BaselineFeature.s0w_q0w] = Features.from(s0w, q0w);
        feats[BaselineFeature.s0w_q0t] = Features.from(q0t, s0w);
        feats[BaselineFeature.s0c_q0w] = Features.from(s0c, q0w);
        feats[BaselineFeature.s0c_q0t] = Features.from(s0c, q0t);

        feats[BaselineFeature.q0w_q1w] = Features.from(q0w, q1w);
        feats[BaselineFeature.q0w_q1t] = Features.from(q1t, q0w);
        feats[BaselineFeature.q0t_q1w] = Features.from(q0t, q1w);
        feats[BaselineFeature.q0t_q1t] = Features.from(q0t, q1t);

        feats[BaselineFeature.s1w_q0w] = Features.from(s1w, q0w);
        feats[BaselineFeature.s1w_q0t] = Features.from(q0t, s1w);
        feats[BaselineFeature.s1c_q0w] = Features.from(s1c, q0w);
        feats[BaselineFeature.s1c_q0t] = Features.from(s1c, q0t);

        // trigram features
        feats[BaselineFeature.s0c_s1c_s2c] = Features.from(s0c, s1c, s2c);
        feats[BaselineFeature.s0w_s1c_s2c] = Features.from(s1c, s2c, s0w);
        feats[BaselineFeature.s0c_s1w_s2c] = Features.from(s0c, s2c, s1w);
        feats[BaselineFeature.s0c_s1c_s2w] = Features.from(s0c, s1c, s2w);

        feats[BaselineFeature.s0c_s1c_q0t] = Features.from(s0c, s1c, q0t);
        feats[BaselineFeature.s0w_s1c_q0t] = Features.from(s1c, q0t, s0w);
        feats[BaselineFeature.s0c_s1w_q0t] = Features.from(s0c, q0t, s1w);
        feats[BaselineFeature.s0c_s1c_q0w] = Features.from(s0c, s1c, q0w);
        return feats;
    }
}
