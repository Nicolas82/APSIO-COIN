package com.wavesplatform.api.http

import akka.NotUsed
import akka.http.scaladsl.server.{Directive0, Route}
import com.wavesplatform.account.{Address, PublicKey}
import com.wavesplatform.api.http.ApiError._
import com.wavesplatform.api.http.requests.DataRequest
import com.wavesplatform.crypto
import com.wavesplatform.api.common.CommonAccountsApi
import com.wavesplatform.settings.RestAPISettings
import com.wavesplatform.utils.{Time, _}
import com.wavesplatform.state.{Blockchain}
import play.api.libs.json._
import com.wavesplatform.wallet.Wallet

import scala.util.{Success, Try}

case class DiplomeApiRoute(
    settings: RestAPISettings,
    blockchain: Blockchain,
    time: Time,
    commonAccountsApi: CommonAccountsApi
) extends ApiRoute {

        import DiplomeApiRoute._

        override lazy val route: Route =
            pathPrefix("diplomes") {
                balances ~ getDiplome
            } 


        //Route qui permet de récupérer tous les diplomes d'un étudiants
        def balances: Route = (path("balances" / AddrSegment) & get){ address => 
            complete(Json.obj(
               "message" -> "La route diplomes/balances est en cours d'implémentation" 
            ))
        }

        //Route qui permet de récupérer le diplome d'un élève  
        def getDiplome: Route = (path("getDiplome" / AddrSegment) & get) { address => 
            complete(Json.obj(
                "message" -> "La route diplomes/get est en cours d'implémentation"
            ))
        }

    }

object DiplomeApiRoute {
    case class Signed(message: String, publicKey: String, signature: String)
}