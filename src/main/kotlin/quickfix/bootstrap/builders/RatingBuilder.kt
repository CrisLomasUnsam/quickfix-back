package quickfix.bootstrap.builders

import quickfix.dto.rating.RatingDTO

class RatingBuilder {
    companion object{

        fun buildMockDTO(jobId: Long, score: Int): RatingDTO {
            return RatingDTO(
                jobId = jobId,
                score = score,
                comment = "Lorem ipsum dolorem em itsu figum",
            )
        }
    }
}