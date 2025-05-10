package cc.allio.turbo.modules.ai.chat.evaluation;

import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.Evaluator;

/**
 * evaluation for response
 *
 * @author j.x
 * @since 0.2.0
 */
public class ResponseEvaluator implements Evaluator {

    @Override
    public EvaluationResponse evaluate(EvaluationRequest evaluationRequest) {
        return null;
    }
}
