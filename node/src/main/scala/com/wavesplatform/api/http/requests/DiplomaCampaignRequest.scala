package com.wavesplatform.api.http.requests

import com.wavesplatform.common.state.ByteStr
import com.wavesplatform.transaction.assets.DiplomaCampaignTransaction.Diplome
import play.api.libs.json.Json

case class DiplomaCampaignRequest(
    version: Option[Byte],
    sender: String,
    diplomes: List[Diplome],
    fee: Long,
    timestamp: Option[Long] = None
)

object DiplomaCampaignRequest {
    implicit val jsonFormat = Json.format[DiplomaCampaignRequest]
}