
package com.wavesplatform.state.diffs

import cats.instances.list._
import cats.instances.map._
import cats.syntax.semigroup._
import cats.syntax.traverse._
import com.wavesplatform.account.Address
import com.wavesplatform.lang.ValidationError
import com.wavesplatform.state._
import com.wavesplatform.transaction.Asset.{IssuedAsset, Waves}
import com.wavesplatform.transaction.TxValidationError.{GenericError, Validation}
import com.wavesplatform.transaction.assets.DiplomaCampaignTransaction.ParsedDiplome
import com.wavesplatform.transaction.assets._

object DiplomaCampaignTransactionDiff {

    def apply(blockchain: Blockchain, blockTime: Long)(tx: DiplomaCampaignTransaction): Either[ValidationError, Diff] = {
        def parseDiplomes(diplome: ParsedDiplome): Validation[(Map[Address, Portfolio], Array[Byte], Array[Byte])] = {
            for {
                
                recipientAddr <-  blockchain.resolveAlias(diplome.recipient)
                portfolio = Map(recipientAddr -> Portfolio(0L, LeaseBalance.empty, Map.empty))

            } yield (portfolio, diplome.name, diplome.description)
        }

        if(true) Right(Diff())
        else Left(GenericError("coucou"))
        //val portfoliosEi = tx.diplomes.toList.traverse(parseDiplomes)

        //portfoliosEi.flatMap { list: List[(Map[Address, Portfolio], Long)] => 
            //val sender  = Address.fromPublicKey(tx.sender)
            //val foldInit = (Map(sender -> Portfolio(-tx.fee, LeaseBalance.empty, Map.empty)), 0L)
            // val (recipientPortfolios, totalAmount) = list.fold(foldInit) { (u, v) => 
            //     //! Chercher la signification de la ligne
            //     (u._1 combine v._1)
            // }
            // val completePortfolio = 
            //     recipientPortfolios
            //         .combine(
            //             tx.assetId
            //                 .fold(Map(sender -> Portfolio()))
            //         )
        //}
    }
}