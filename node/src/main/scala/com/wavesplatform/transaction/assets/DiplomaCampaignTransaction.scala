package com.wavesplatform.transaction.assets

import com.google.protobuf.ByteString
import cats.instances.list._
import cats.syntax.traverse._
import com.wavesplatform.account._
import com.wavesplatform.crypto
import com.wavesplatform.lang.ValidationError
import com.wavesplatform.transaction._
import com.wavesplatform.transaction.TxValidationError._
import com.wavesplatform.transaction.serialization.impl.DiplomaCampaignTxSerializer
import com.wavesplatform.transaction.assets.DiplomaCampaignTransaction.ParsedDiplome
import com.wavesplatform.transaction.validation.TxValidator
import com.wavesplatform.transaction.validation.impl.DiplomaCampaignTxValidator
import play.api.libs.json.{JsObject, Json}
import monix.eval.Coeval

import scala.util.{Either, Try}


case class DiplomaCampaignTransaction(
    version: TxVersion,
    sender: PublicKey,
    diplomes: Seq[ParsedDiplome],
    fee: TxAmount,
    timestamp: TxTimestamp,
    proofs: Proofs,
    chainId: Byte
) extends ProvenTransaction
    with VersionedTransaction
    with TxWithFee.InWaves
    with FastHashId
    with LegacyPBSwitch.V2{

        override val builder = DiplomaCampaignTransaction

        override val bodyBytes: Coeval[Array[Byte]] = Coeval.evalOnce(builder.serializer.bodyBytes(this))
        override val bytes: Coeval[Array[Byte]]     = Coeval.evalOnce(builder.serializer.toBytes(this))
        override val json: Coeval[JsObject]         = Coeval.evalOnce(builder.serializer.toJson(this))

        //TODO: Faire une fonction compact
        //def compactJson()

}

object DiplomaCampaignTransaction extends TransactionParser {
    type TransactionT = DiplomaCampaignTransaction

    val MaxDiploma                  = 100
    val MinDiplomaNameLength        = 4
    val MaxDiplomaNameLength        = 16
    val MaxDiplomaDescriptionLength = 1000


    override val typeId: TxType                    = 18: Byte
    override val supportedVersions: Set[TxVersion] = Set(1, 2)

    implicit val validator: TxValidator[DiplomaCampaignTransaction] = DiplomaCampaignTxValidator

    implicit def sign(tx: DiplomaCampaignTransaction, privateKey: PrivateKey): DiplomaCampaignTransaction = 
        tx.copy(proofs = Proofs(crypto.sign(privateKey, tx.bodyBytes())))

    val serializer = DiplomaCampaignTxSerializer

    override def parseBytes(bytes: Array[Byte]): Try[DiplomaCampaignTransaction] = 
        serializer.parseBytes(bytes)

    case class Diplome(
        assetId: String,
        sender: String,
        recipient: String,
        name: Array[Byte],
        description: Array[Byte]
    )

    object Diplome {
        implicit val jsonFormat = Json.format[Diplome]
    }

    case class ParsedDiplome(assetId: String, sender: AddressOrAlias, recipient: AddressOrAlias, name: Array[Byte], description: Array[Byte])

    def create(
        version: TxVersion,
        sender: PublicKey,
        diplomes: Seq[ParsedDiplome],
        fee: TxAmount,
        timestamp: TxTimestamp,
        proofs: Proofs,
        chainId: Byte = AddressScheme.current.chainId
    ): Either[ValidationError, DiplomaCampaignTransaction] = 
        DiplomaCampaignTransaction(version, sender, diplomes, fee, timestamp, proofs, chainId).validatedEither

    def signed(
        version: TxVersion,
        sender: PublicKey,
        diplomes: Seq[ParsedDiplome],
        fee: TxAmount,
        timestamp: TxTimestamp,
        signer: PrivateKey
    ): Either[ValidationError, DiplomaCampaignTransaction] = 
        create(version, sender, diplomes, fee, timestamp, Proofs.empty).map(_.signWith(signer))

    def selfSigned(
        version: TxVersion,
        sender: KeyPair,
        diplomes: Seq[ParsedDiplome],
        fee: TxAmount,
        timestamp: TxTimestamp,
    ): Either[ValidationError, DiplomaCampaignTransaction] = 
        signed(version, sender.publicKey, diplomes, fee, timestamp, sender.privateKey)

    //TODO: parsed diplôme
    def parseDiplomesList(diplomes: List[Diplome]): Validation[List[ParsedDiplome]] = {
        diplomes.traverse {
            case Diplome(assetId, sender, recipient, name, description) => 
                AddressOrAlias(fromString())
        }
    }

}