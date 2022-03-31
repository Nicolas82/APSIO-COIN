package com.wavesplatform.transaction.validation.impl

import cats.data.Validated
import com.google.protobuf.ByteString
import com.wavesplatform.transaction.TxValidationError.GenericError
import com.wavesplatform.transaction.assets.DiplomaCampaignTransaction
import com.wavesplatform.transaction.assets.DiplomaCampaignTransaction.{MaxDiploma, MinDiplomaNameLength, MaxDiplomaNameLength, MaxDiplomaDescriptionLength}
import com.wavesplatform.transaction.validation.{TxValidator, ValidatedV}

object DiplomaCampaignTxValidator extends TxValidator[DiplomaCampaignTransaction] {
    override def validate(tx: DiplomaCampaignTransaction): ValidatedV[DiplomaCampaignTransaction] = {
        
        def diplomaDescription(description: Array[Byte]):Boolean = {
            description.size <= MaxDiplomaDescriptionLength
        }

        def diplomaName(name: Array[Byte]): Boolean = {
            name.size >= MinDiplomaNameLength && name.size <= MaxDiplomaNameLength
        }
        
        import tx._
        V.seq(tx)(
            V.cond(diplomes.forall( d => {diplomaName(d.name)}), GenericError("The name of the asset is too long")),
            V.cond(diplomes.forall( d => {diplomaDescription(d.description)}), GenericError("The description of the asset is too long")),
            V.cond(diplomes.length <= MaxDiploma, GenericError(s"Number of Diplomes ${diplomes.length} is greater than $MaxDiploma")),
            V.fee(fee),
            V.chainIds(chainId, diplomes.map(_.sender.chainId): _*),
            V.chainIds(chainId, diplomes.map(_.recipient.chainId): _*)
        )
    }

}