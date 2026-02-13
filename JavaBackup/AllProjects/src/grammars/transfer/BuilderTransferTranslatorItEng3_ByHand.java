package grammars.transfer;

import grammars.ElemGrammarBase;
import grammars.NodeParsedSentence;
import tools.SynonymSet;

/** Le regole di transfer IT -> ENG sono definite a mano. */
public class BuilderTransferTranslatorItEng3_ByHand extends BuilderTransferTranslatorItEng {
	public static final BuilderTransferTranslatorItEng3_ByHand SINGLETON = new BuilderTransferTranslatorItEng3_ByHand();
	public static final ATransferTranslationRuleBased TRANSFERER_RULE_BASED = SINGLETON.newTransferItEng();

	//

	//

	//

	private BuilderTransferTranslatorItEng3_ByHand() {}

	@Override
	public ATransferTranslationRuleBased newTransferItEng() {
		ATransferTranslationRuleBased t;
//		t = new TransferTranslationRuleBased();
//		t = new TransferTranslationRuleBased_V2();
//		t = new TransferTranslationRuleBased_V3();
		t = new TransferTranslationRuleBased_V4();

		// identities
		for (ElemGrammarBase egb : ElemGrammarBase.values()) {
			t.addRule(new IdentityTransferRule(egb));
		}

//		t.addRule(new TransferRule(NodeParsedSentence.newNSD(ElemGrammarBase.PUNC.getElemGrammarBase())) {
//			@Override
//			public  SubTransferResult implementsTransferRule(TransferTranslationItEng3 transferer,
//					NodeParsedSentence originalSubtree) {SubTransferResult r;
//				NodeParsedSentence newNode;
//				newNode = NodeParsedSentence.newNSD(ElemGrammarBase.PUNC.getElemGrammarBase());
//				r.addPairOldNewNode(originalSubtree, newNode);
//				return newNode;
//			}
//		});

		//
		// more complexity
		//

		t.addRule(new TransferRule((NodeParsedSentence) ElemGrammarBase.Verb.newNode()//
				.addChildNC(ElemGrammarBase.Noun.newNode().addAlternatives(ElemGrammarBase.Subject.getSynonyms()))) {
			@Override
			public SubTransferResult implementsTransferRule(ATransferTranslationRuleBased transferer,
					NodeParsedSentence originalSubtree) {
				SubTransferResult r;
				NodeParsedSentence newVerb, newSubj, oldSubj;
				// prodicing
				newVerb = originalSubtree.clone(); // ElemGrammarBase.Verb.newNSD();
				// (the old child must be taken)
				oldSubj = (NodeParsedSentence) originalSubtree
						.getChildNCMostSimilarTo(ElemGrammarBase.Noun.getSynonyms());
				if (oldSubj == null) {
					oldSubj = (NodeParsedSentence) originalSubtree
							.getChildNCMostSimilarTo(ElemGrammarBase.Subject.getSynonyms());
				}
				newSubj = oldSubj.clone();
				// wiring
				newVerb.addChildNC(newSubj);
				// mandatory invoke .. (bottom-up is better, but You are free)
				r = new SubTransferResult(newVerb);
				r.addPairOldNewNode(oldSubj, newSubj);
				r.addPairOldNewNode(originalSubtree, newVerb);
				return r;
			}
		});

		t.addRule(new TransferRule(//
				(NodeParsedSentence) ElemGrammarBase.Verb.newNode()//
						.addChildNC(ElemGrammarBase.Objectt.newNode())//
		) {
			@Override
			public SubTransferResult implementsTransferRule(ATransferTranslationRuleBased transferer,
					NodeParsedSentence originalSubtree) {
				SubTransferResult r;
				NodeParsedSentence newVerb, newSubj, newObj, oldObj;
				// prodicing
				newVerb = ElemGrammarBase.Verb.newNode();
				// (the old child must be taken)
				oldObj = (NodeParsedSentence) originalSubtree
						.getChildNCMostSimilarTo(ElemGrammarBase.Objectt.getSynonyms());
				newObj = oldObj.clone();
				// wiring
				newVerb.addChildNC(newObj);

				/*
				 * in italiano, esiste il soggetto sottointeso, in inglese no -> creiamolo e
				 * mettiamolo di default, con il genere che dipende dall'oggetto
				 */
				newSubj = ElemGrammarBase.Subject.newNode();
				String[] genderFeature = oldObj.getFeatures().get("Gender");
				if (genderFeature != null) { newSubj.addFeatures("Gender", genderFeature); }
				newVerb.addChildNC(newSubj);

				// mandatory invoke .. (bottom-up is better, but You are free)
				r = new SubTransferResult(newVerb);
				r.addPairOldNewNode(
						(NodeParsedSentence) originalSubtree
								.getChildNCMostSimilarTo(ElemGrammarBase.Subject.getSynonyms()), //
						newSubj);
				r.addPairOldNewNode(oldObj, newObj);
				r.addPairOldNewNode(originalSubtree, newVerb);
				return r;
			}
		});

		t.addRule(new TransferRule(//
				(NodeParsedSentence) ElemGrammarBase.Subject.newNode()
						.addAlternatives(ElemGrammarBase.Noun.getSynonyms())//
						.addChildNC(ElemGrammarBase.Adjective.newNode())//
		) {
			@Override
			public SubTransferResult implementsTransferRule(ATransferTranslationRuleBased transferer,
					NodeParsedSentence originalSubtree) {
				SubTransferResult r;
				NodeParsedSentence newSubj, newAdj, oldAdj;
				// prodicing
				newSubj = ElemGrammarBase.Subject.newNode();
				// (the old child must be taken)
				oldAdj = (NodeParsedSentence) originalSubtree
						.getChildNCMostSimilarTo(ElemGrammarBase.Adjective.getSynonyms());
				newAdj = ElemGrammarBase.Adjective.newNode(); // TODO sostituire col clone del nodo Adj figlio di
																// originalSubtree
				// wiring
				newSubj.addChildNC(newAdj);
				// mandatory invoke .. (bottom-up is better, but You are free)
				r = new SubTransferResult(newSubj);
				r.addPairOldNewNode(oldAdj, newAdj);
				r.addPairOldNewNode(originalSubtree, newSubj);
				return r;
			}
		});

//		t.addRule(new TransferRule((NodeParsedSentence) // casting
//		NodeParsedSentence.newNSD(new ElementGrammarWithAlternatives(new String[] { "V", "verb" }))
//				// 1st level of nesting
//				.addChildNC(
//						NodeParsedSentence.newNSD(new ElementGrammarWithAlternatives(new String[] { "S", "subj" })))
//		//
//		) {
//			@Override
//			public  SubTransferResult implementsTransferRule(NodeParsedSentence originalSubtree) {SubTransferResult r;
////				return NodeParsedSentence.newNSD(new ElementGrammarWithAlternatives(new String[] { "V", "verb" }));
//				return (NodeParsedSentence) //
//				NodeParsedSentence.newNSD(new ElementGrammarWithAlternatives(new String[] { "V", "verb" }))
//						// 1st level of nesting
//						.addChildNC(NodeParsedSentence
//								.newNSD(new ElementGrammarWithAlternatives(new String[] { "S", "subj" })));
//			}
//		});

		//
		// EVEN MORE COMPLEX
		//

		/*
		 * TODO WHY IS NOT BEING ADDED?? AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH
		 */
		System.out.println("\n\n\n BUILDER BY HAND - 3-CHILD FILLED TREES :D\n\n");

		t.addRule(new TransferRule(//
				(NodeParsedSentence) ElemGrammarBase.Verb.newNode()//
						.addChildNC(ElemGrammarBase.Subject.newNode())//
						.addChildNC(ElemGrammarBase.Objectt.newNode())//
		) {
			@Override
			public SubTransferResult implementsTransferRule(ATransferTranslationRuleBased transferer,
					NodeParsedSentence originalSubtree) {
				SubTransferResult r;
				NodeParsedSentence newVerb, newSubj, oldSubj, newObj, oldObj;
				System.out.println("----- siiiiii :D mi hanno sceltooooo :D ");
				// prodicing
				newVerb = ElemGrammarBase.Verb.newNode();
				// (the old child must be taken)
				oldSubj = (NodeParsedSentence) originalSubtree
						.getChildNCMostSimilarTo(ElemGrammarBase.Subject.getSynonyms());
				oldObj = (NodeParsedSentence) originalSubtree
						.getChildNCMostSimilarTo(ElemGrammarBase.Objectt.getSynonyms());
				// and then new ones
				newSubj = ElemGrammarBase.Subject.newNode();
				newObj = ElemGrammarBase.Objectt.newNode();
				// wiring
				newVerb.addChildNC(newSubj);
				newVerb.addChildNC(newObj);
				// mandatory invoke .. (bottom-up is better, but You are free)
				r = new SubTransferResult(newVerb);
				r.addPairOldNewNode(oldSubj, newSubj);
				r.addPairOldNewNode(oldObj, newObj);
				r.addPairOldNewNode(originalSubtree, newVerb);
				return r;
			}
		});

		System.out.println("\n\n builder by hand ... 4 children or more complexities\n\n");

		t.addRule(new TransferRule(//
				(NodeParsedSentence) ElemGrammarBase.Verb.newNode()//
						.addChildNC(ElemGrammarBase.Subject.newNode())//
						.addChildNC(ElemGrammarBase.Aux.newNode())//
						.addChildNC(ElemGrammarBase.Objectt.newNode())//
		) {
			@Override
			public SubTransferResult implementsTransferRule(ATransferTranslationRuleBased transferer,
					NodeParsedSentence originalSubtree) {
				SubTransferResult r;
				NodeParsedSentence newVerb, newSubj, oldSubj, newObj, oldObj, newAux, oldAux;
				// prodicing
				newVerb = ElemGrammarBase.Verb.newNode();
				// (the old child must be taken)
				oldSubj = (NodeParsedSentence) originalSubtree
						.getChildNCMostSimilarTo(ElemGrammarBase.Subject.getSynonyms());
				oldObj = (NodeParsedSentence) originalSubtree
						.getChildNCMostSimilarTo(ElemGrammarBase.Objectt.getSynonyms());
				oldAux = (NodeParsedSentence) originalSubtree
						.getChildNCMostSimilarTo(ElemGrammarBase.Aux.getSynonyms());
				// and then new ones
				newSubj = ElemGrammarBase.Subject.newNode();
				newAux = ElemGrammarBase.Aux.newNode();
				newObj = ElemGrammarBase.Objectt.newNode();
				// wiring
				newVerb.addChildNC(newSubj);
				newVerb.addChildNC(newObj);
				newVerb.addChildNC(newAux);
				// mandatory invoke .. (bottom-up is better, but You are free)
				r = new SubTransferResult(newVerb);
				r.addPairOldNewNode(oldSubj, newSubj);
				r.addPairOldNewNode(oldObj, newObj);
				r.addPairOldNewNode(oldAux, newAux);
				r.addPairOldNewNode(originalSubtree, newVerb);
				return r;
			}
		});

		t.addRule(new TransferRule(//
				(NodeParsedSentence) ElemGrammarBase.Verb.newNode()//
						.addChildNC(
								ElemGrammarBase.Aux.newNode().addAlternatives(ElemGrammarBase.Subject.getSynonyms()))//
		) {
			@Override
			public SubTransferResult implementsTransferRule(ATransferTranslationRuleBased transferer,
					NodeParsedSentence originalSubtree) {
				SubTransferResult r;
				NodeParsedSentence newVerb, newAux, oldAux;
				// prodicing
				newVerb = ElemGrammarBase.Verb.newNode();
				r = new SubTransferResult(newVerb);
				// (the old child must be taken)
				SynonymSet auxSyns;
				auxSyns = ElemGrammarBase.Aux.getSynonymsClone();
				ElemGrammarBase.Subject.getSynonyms().forEach(auxSyns::addAlternative);
				oldAux = (NodeParsedSentence) originalSubtree.getChildNCMostSimilarTo(auxSyns);
				if (oldAux != null) {

				}
				// TODO sistemare il soggetto del verbo
//				if (oldAux != null && "VA".equals(oldAux.getPos())) {
//					// and then new ones
//					newAux = oldAux.clone(); // new NodeParsedSentence(auxSyns);
//					// wiring
//					newVerb.addChildNC(newAux);
//					r.addPairOldNewNode(oldAux, newAux);
//				} else if (oldAux == null) {
//					System.out.println("\n\n I'M NULL HERE 2, WITH ORIGINAL SUBTREE:");
//					System.out.println(originalSubtree);
//				}
				r.addPairOldNewNode(originalSubtree, newVerb);
				return r;
			}
		});

		// forme verbali composte?
		t.addRule(new TransferRule(//
				(NodeParsedSentence) ElemGrammarBase.Verb.newNode()//
						.addChildNC(ElemGrammarBase.Aux.newNode().addAlternatives(ElemGrammarBase.Verb.getSynonyms())
								.addAlternative("VA"))//
		) {
			@Override
			public SubTransferResult implementsTransferRule(ATransferTranslationRuleBased transferer,
					NodeParsedSentence originalSubtree) {
				SubTransferResult r;
				NodeParsedSentence newVerb, newAux, oldAux;
				// prodicing
				newVerb = ElemGrammarBase.Verb.newNode();
				r = new SubTransferResult(newVerb);
				// (the old child must be taken)
				SynonymSet auxSyns;
				auxSyns = ElemGrammarBase.Aux.getSynonymsClone();
				ElemGrammarBase.Verb.getSynonyms().forEach(auxSyns::addAlternative);
				oldAux = (NodeParsedSentence) originalSubtree.getChildNCMostSimilarTo(auxSyns);
				if (oldAux != null && "VA".equals(oldAux.getPos())) {
					// and then new ones
					newAux = oldAux.clone(); // new NodeParsedSentence(auxSyns);
					// wiring
					newVerb.addChildNC(newAux);
					r.addPairOldNewNode(oldAux, newAux);
				} else if (oldAux == null) {
					System.out.println("\n\n I'M NULL HERE, WITH ORIGINAL SUBTREE:");
					System.out.println(originalSubtree);
				}
				r.addPairOldNewNode(originalSubtree, newVerb);
				return r;
			}
		});

		t.addRule(new TransferRule(//
				(NodeParsedSentence) ElemGrammarBase.Verb.newNode()//
						.addChildNC(//
								ElemGrammarBase.Subject.newNode().addAlternatives(ElemGrammarBase.Noun.getSynonyms())//
										.addChildNC(ElemGrammarBase.Aux.newNode()//
												.addAlternatives(ElemGrammarBase.Det.getSynonyms())
												.addAlternatives(ElemGrammarBase.Comp.getSynonyms()))//
						)//
						.addChildNC(
								ElemGrammarBase.Objectt.newNode().addAlternatives(ElemGrammarBase.Noun.getSynonyms()))//
		) {
			@Override
			public SubTransferResult implementsTransferRule(ATransferTranslationRuleBased transferer,
					NodeParsedSentence originalSubtree) {
				SubTransferResult r;
				NodeParsedSentence newVerb, newSubj, oldSubj, newObj, newAux, oldObj, oldAux;
				// prodicing
				newVerb = originalSubtree.clone();
				r = new SubTransferResult(newVerb);
				r.addPairOldNewNode(originalSubtree, newVerb);
				// (the old child must be taken)
				oldSubj = (NodeParsedSentence) originalSubtree
						.getChildNCMostSimilarTo(ElemGrammarBase.Subject.getSynonyms());
				if (oldSubj == null) {
					oldSubj = (NodeParsedSentence) originalSubtree
							.getChildNCMostSimilarTo(ElemGrammarBase.Noun.getSynonyms());
				}
				newSubj = (oldSubj != null) ? oldSubj.clone() : ElemGrammarBase.Subject.newNode();
				newVerb.addChildNC(newSubj);
				r.addPairOldNewNode(oldSubj, newSubj);
				newAux = null;
				if (oldSubj != null) { // the det/aux
					NodeParsedSentence nnn = null;
//					SynonymSet squery;
					// ElemGrammarBase[]
					nnn = (NodeParsedSentence) ElemGrammarBase.Aux.newNode()//
							.addAlternatives(ElemGrammarBase.Det.getSynonyms())
							.addAlternatives(ElemGrammarBase.Comp.getSynonyms());
//					squery = nnn.getKeyIdentifier();
					oldAux = (NodeParsedSentence) oldSubj.getChildNCMostSimilarTo(nnn);
					if (oldAux != null) {
						newAux = oldAux.clone();
						r.addPairOldNewNode(oldAux, newAux);
					}
				}
				if (newAux == null) { newAux = ElemGrammarBase.Aux.newNode(); }
				newVerb.addChildNC(newAux);
				oldObj = (NodeParsedSentence) originalSubtree
						.getChildNCMostSimilarTo(ElemGrammarBase.Objectt.getSynonyms());
// and then new ones
				newObj = oldObj != null ? oldObj.clone() : ElemGrammarBase.Objectt.newNode();
				newVerb.addChildNC(newObj);

				// mandatory invoke .. (bottom-up is better, but You are free)
				r.addPairOldNewNode(oldObj, newObj);
				return r;
			}
		});

		t.addRule(new TransferRule(//
				(NodeParsedSentence) ElemGrammarBase.Verb.newNode()//
						.addChildNC(//
								ElemGrammarBase.Subject.newNode().addAlternatives(ElemGrammarBase.Noun.getSynonyms())//
										.addChildNC(ElemGrammarBase.Aux.newNode()//
												.addAlternatives(ElemGrammarBase.Det.getSynonyms())
												.addAlternatives(ElemGrammarBase.Comp.getSynonyms()))//
						)//
						.addChildNC(
								ElemGrammarBase.Objectt.newNode().addAlternatives(ElemGrammarBase.Noun.getSynonyms()))//
						.addChildNC(ElemGrammarBase.Adverb.newNode())//
		) {
			@Override
			public SubTransferResult implementsTransferRule(ATransferTranslationRuleBased transferer,
					NodeParsedSentence originalSubtree) {
				SubTransferResult r;
				NodeParsedSentence newVerb, newSubj, oldSubj, newObj, oldObj, newAux, oldAux, newAdv, oldAdv;
				// prodicing
				newVerb = originalSubtree.clone();
				r = new SubTransferResult(newVerb);
				r.addPairOldNewNode(originalSubtree, newVerb);
				// (the old child must be taken)
				oldSubj = (NodeParsedSentence) originalSubtree
						.getChildNCMostSimilarTo(ElemGrammarBase.Subject.getSynonyms());
				if (oldSubj == null) {
					oldSubj = (NodeParsedSentence) originalSubtree
							.getChildNCMostSimilarTo(ElemGrammarBase.Noun.getSynonyms());
				}
				newSubj = (oldSubj != null) ? oldSubj.clone() : ElemGrammarBase.Subject.newNode();
				newVerb.addChildNC(newSubj);
				r.addPairOldNewNode(oldSubj, newSubj);
				newAux = null;
				if (oldSubj != null) { // the det/aux
					NodeParsedSentence nnn = null;
//					SynonymSet squery;
					// ElemGrammarBase[]
					nnn = (NodeParsedSentence) ElemGrammarBase.Aux.newNode()//
							.addAlternatives(ElemGrammarBase.Det.getSynonyms())
							.addAlternatives(ElemGrammarBase.Comp.getSynonyms());
//					squery = nnn.getKeyIdentifier();
					oldAux = (NodeParsedSentence) oldSubj.getChildNCMostSimilarTo(nnn);
					if (oldAux != null) {
						newAux = oldAux.clone();
						r.addPairOldNewNode(oldAux, newAux);
					}
				}
				if (newAux == null) { newAux = ElemGrammarBase.Aux.newNode(); }
				newVerb.addChildNC(newAux);
				oldObj = (NodeParsedSentence) originalSubtree
						.getChildNCMostSimilarTo(ElemGrammarBase.Objectt.getSynonyms());
// and then new ones
				newObj = oldObj != null ? oldObj.clone() : ElemGrammarBase.Objectt.newNode();
				newVerb.addChildNC(newObj);
				r.addPairOldNewNode(oldObj, newObj);

				oldAdv = (NodeParsedSentence) originalSubtree
						.getChildNCMostSimilarTo(ElemGrammarBase.Adverb.getSynonyms());
				newAdv = (oldAdv != null) ? oldAdv.clone() : ElemGrammarBase.Adverb.newNode();
				return r;
			}
		});


//
//		t.addRule(new TransferRule(
//				//
//				(NodeParsedSentence) ElemGrammarBase.Verb.newNSD() //
//						.addChildNC( ElemGrammarBase.Noun.newNSD())//
//						// second child of verb:
//						.addChildNC(ElemGrammarBase.Noun.)//
//		) {
//			@Override
//			public  SubTransferResult implementsTransferRule(ATransferTranslationRuleBased transferer,
//					NodeParsedSentence originalSubtree) {SubTransferResult r;
//				NodeParsedSentence newVerb, newSubj, newObj, oldSubj, oldObjs;
//				// prodicing
//				newVerb = ElemGrammarBase.Verb.newNSD();
//				// (the old child must be taken)
//				oldSubj = (NodeParsedSentence) originalSubtree.getChildNCMostSimilarTo(ElemGrammarBase.Subject.getElemGrammarBase());
//				if (oldSubj == null) {
//					oldSubj = (NodeParsedSentence) originalSubtree.getChildNCMostSimilarTo(ElemGrammarBase.Noun.getElemGrammarBase());
//				}
//				oldObjs = (NodeParsedSentence) originalSubtree.getChildNCMostSimilarTo(ElemGrammarBase.Objectt.getElemGrammarBase());
//				if (oldObjs == null) {
//					oldObjs = (NodeParsedSentence) originalSubtree.getChildNCMostSimilarTo(ElemGrammarBase.Noun.getElemGrammarBase());
//					// warning : is it the same ov previous?
//				}
//				newSubj = NodeParsedSentence.newNSD(ElemGrammarBase.Subject.getElemGrammarBase());
//				newObj = NodeParsedSentence.newNSD(ElemGrammarBase.Objectt.getElemGrammarBase());
//				// wiring
//				newVerb.addChildNC(newSubj);
//				newVerb.addChildNC(newObj);
//				// mandatory invoke .. (bottom-up is better, but You are free)
//				r.addPairOldNewNode(oldSubj, newSubj);
//				r.addPairOldNewNode(oldObjs, newObj);
//				r.addPairOldNewNode(originalSubtree, newVerb);
//				return r;
//			}
//		});
		return t;
	}

//

	protected static class IdentityTransferRule extends TransferRule {
		final ElemGrammarBase egb;

		public IdentityTransferRule(ElemGrammarBase egb) {
			super(egb.newNode());
			this.egb = egb;
		}

		@Override
		public SubTransferResult implementsTransferRule(ATransferTranslationRuleBased transferer,
				NodeParsedSentence originalSubtree) {
			NodeParsedSentence newNode;
			newNode = originalSubtree.clone(); // egb.newNSD();
			return new SubTransferResult(newNode).addPairOldNewNode(originalSubtree, newNode);
		}
	}
}