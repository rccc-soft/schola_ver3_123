package com.example.schola_ver3;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvaluationView {

    private ReviewManager reviewManager;
    private ExecutorService executorService;

    public EvaluationView(Context context) {
        this.reviewManager = new ReviewManager(context);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * 評価スコアを非同期で取得し、コールバックで結果を返すメソッド
     *
     * @param userMemberId ユーザーのmemberID
     * @param callback     評価スコア取得後に実行されるコールバック
     */
    public void fetchAndDisplayReviewScore(int userMemberId, ReviewScoreCallback callback) {
        executorService.execute(() -> {
            try {
                // 評価平均を取得
                double averageScore = reviewManager.getAverageReviewScore(userMemberId);

                // 評価スコアを整数に丸める
                int roundedScore = (int) Math.round(averageScore);
                roundedScore = Math.max(1, Math.min(roundedScore, 5)); // 1から5の範囲に制限

                // 個別のレビュー点数を取得
                List<Integer> reviewScores = reviewManager.getReviewScores(userMemberId);

                // コールバックを実行
                callback.onScoreFetched(averageScore, roundedScore, reviewScores);
            } catch (Exception e) {
                Log.e("EvaluationView", "Error fetching review score", e);
                callback.onError(e);
            }
        });
    }

    /**
     * コールバックインターフェース
     */
    public interface ReviewScoreCallback {
        void onScoreFetched(double averageScore, int roundedScore, List<Integer> reviewScores);

        void onError(Exception e);
    }

    /**
     * ExecutorServiceのシャットダウンメソッド
     */
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        if (reviewManager != null) {
            reviewManager.close();
        }
    }
}
