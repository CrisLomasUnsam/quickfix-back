package quickfix.utils.events

import quickfix.models.Rating

class OnRatingEditedEvent(
    val rating: Rating
)