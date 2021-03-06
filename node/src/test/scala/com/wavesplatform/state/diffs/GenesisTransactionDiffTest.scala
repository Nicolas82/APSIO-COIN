package com.wavesplatform.state.diffs

import cats._
import com.wavesplatform.db.WithState
import com.wavesplatform.lagonaki.mocks.TestBlock
import com.wavesplatform.state._
import com.wavesplatform.test.PropSpec
import org.scalacheck.Gen

class GenesisTransactionDiffTest extends PropSpec with WithState {
  def nelMax[T](g: Gen[T], max: Int = 10): Gen[List[T]] = Gen.choose(1, max).flatMap(Gen.listOfN(_, g))

  property("fails if height != 1") {
    forAll(genesisGen, positiveIntGen suchThat (_ > 1)) { (gtx, h) =>
      GenesisTransactionDiff(h)(gtx) should produce("GenesisTransaction cannot appear in non-initial block")
    }
  }

  property("Diff establishes Waves invariant") {
    forAll(nelMax(genesisGen)) { gtxs =>
      assertDiffAndState(Seq.empty, TestBlock.create(gtxs)) { (blockDiff, _) =>
        val totalPortfolioDiff: Portfolio = Monoid.combineAll(blockDiff.portfolios.values)
        totalPortfolioDiff.balance shouldBe gtxs.map(_.amount).sum
        totalPortfolioDiff.effectiveBalance shouldBe gtxs.map(_.amount).sum
        totalPortfolioDiff.assets shouldBe Map.empty

        gtxs.foreach { gtx =>
          blockDiff.portfolios(gtx.recipient).balance shouldBe gtx.amount
        }
      }
    }
  }
}
