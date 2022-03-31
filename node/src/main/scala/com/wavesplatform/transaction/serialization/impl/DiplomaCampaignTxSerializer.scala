package com.wavesplatform.transaction.serialization.impl

import java.nio.ByteBuffer

import com.google.common.primitives.{Bytes, Longs, Shorts}
import com.wavesplatform.account.{AddressOrAlias, AddressScheme}
import com.wavesplatform.common.state.ByteStr
import com.wavesplatform.common.utils._
import com.wavesplatform.serialization._
import com.wavesplatform.serialization.{ByteBufferOps, Deser}

import com.wavesplatform.transaction.TxVersion
import com.wavesplatform.transaction.assets.DiplomaCampaignTransaction
import com.wavesplatform.transaction.assets.DiplomaCampaignTransaction.{ParsedDiplome, Diplome}
import com.wavesplatform.utils.byteStrFormat
import play.api.libs.json.{JsObject, JsValue, Json}

import scala.util.Try

object DiplomaCampaignTxSerializer {

    //TODO: faire la fonction d'enregistremenets des diplomes créées 
    def diplomesJson(diplomes: Seq[ParsedDiplome]) : JsValue = 
        Json.toJson(diplomes.map { 
            case ParsedDiplome(assetId, sender, recipient, name, description) => 
                Diplome(assetId, sender.stringRepr, recipient.stringRepr, name, description) 
            })

    def toJson(tx: DiplomaCampaignTransaction): JsObject = {
        import tx._
        //! Ajouter la transaction à ce fichier
        BaseTxJson.toJson(tx) ++ Json.obj(
            "diplomesCount" -> diplomes.size,
            "diplomes"      -> diplomesJson(diplomes)
        )
    }

    def bodyBytes(tx: DiplomaCampaignTransaction): Array[Byte] = {
        import tx._
        version match{
            case TxVersion.V1 => 
                val diplomesBytes = diplomes.map { case ParsedDiplome(assetId, sender, recipient, name, description) => Bytes.concat(assetId.getBytes(), sender.bytes, recipient.bytes, name, description)}

                Bytes.concat(
                    Array(builder.typeId, version),
                    sender.arr,
                    Bytes.concat(diplomesBytes: _*),
                    Longs.toByteArray(fee),
                    Longs.toByteArray(timestamp)
                )

           case _ => 
                PBTransactionSerializer.bodyBytes(tx)
        }
    }

    def toBytes(tx: DiplomaCampaignTransaction): Array[Byte] = 
        if(tx.isProtobufVersion) PBTransactionSerializer.bytes(tx)
        else Bytes.concat(this.bodyBytes(tx), tx.proofs.bytes())

    def parseBytes(bytes: Array[Byte]): Try[DiplomaCampaignTransaction] = Try {
        def parseDiplomes(buf: ByteBuffer): Seq[DiplomaCampaignTransaction.ParsedDiplome] = {
            def readDiplome(buf: ByteBuffer): ParsedDiplome = {
                //TODO: les valeurs à parser
                val assetId     = new String(Deser.parseArrayWithLength(buf))
                val sender      = AddressOrAlias.fromBytes(buf).explicitGet()
                val recipient   = AddressOrAlias.fromBytes(buf).explicitGet()
                val name        = Deser.parseArrayWithLength(buf)
                val description = Deser.parseArrayWithLength(buf)
                ParsedDiplome(
                    assetId,
                    sender,
                    recipient,
                    name,
                    description
                )
            }

            val entryCount = buf.getShort
            require(entryCount >= 0 && buf.remaining() > entryCount, s"Broken array size ($entryCount entries while ${buf.remaining()} bytes available)")
            Vector.fill(entryCount)(readDiplome(buf))
        }

        val buf = ByteBuffer.wrap(bytes)
        require(buf.getByte == DiplomaCampaignTransaction.typeId && buf.getByte == TxVersion.V1, "transaction type mismatch")

        val sender    = buf.getPublicKey
        val diplomes  = parseDiplomes(buf)
        val timestamp = buf.getLong
        val fee       = buf.getLong
        val proofs    = buf.getProofs
        DiplomaCampaignTransaction(TxVersion.V1, sender, diplomes, fee, timestamp, proofs, AddressScheme.current.chainId)
    }

}