package quickfix.utils.events

import quickfix.models.Rating

class OnRatingReceivedEvent(
    val rating: Rating
)