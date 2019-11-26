package umontreal.ssj.markovchainrqmc.florian.chemicalReactionNetworks;

import umontreal.ssj.util.sort.florian.MultDimToOneDimSort;

/**
 * Implements a #MultDimToOneDimSort for the #PKA class, where the score
 * function is a multivariate polynomial. The coefficients to define the
 * polynomial can either be passed in the constructor or, for the specific
 * example in our paper, taken from a predefined set (example specific!).
 * 
 * 
 * @author florian
 *
 */
public class PKASort extends MultDimToOneDimSort {

	/**
	 * The coefficients of the multivariate polynomial that defines the score
	 * function. The order must match the order of the #varIndices.
	 */
	double[] coeffs;
	/**
	 * Whether the polynomial has a constant term or ot.
	 */
	boolean bias;
	/**
	 * The coordinate indices that define the monomials in the polynomial. For
	 * instance, the tuple @f$(i,j,k)@f$ identifies the monomial $x_ix_jx_k$.
	 */
	int[][] varIndices;

	// format: {{b_0^(1),b_0^(2),...,b_0^(numSteps-1)}, ...
	// ,{b_numCoeffs^(1),b_numCoeffs^(2),...,b_numCoeffs^(numSteps-1)}}
	// n=2^20, with bias, smoothing factor 0.8
//	private final double[][] coeffMat = {
//			{ 2.1910674115697408E8, 1.687740066599372E7, -4.5579812903126374E7, -3.582793252817783E7,
//					-9226894.356760569, -3366872.4722058866, -1.613554788973878E7, -1.4816556148425477E7,
//					2098802.858222615, 2.9081890938240997E7, 4.1640834039446674E7, 3.815871297480067E7,
//					1.9976097402696334E7, -3146741.8848482147 },
//			{ -5850.792272669234, -165.0150905787632, 1439.647151391437, 1049.4341146987008, 202.2111130995845,
//					72.7992705318886, 564.5442627445827, 514.8023359742485, -119.96004516749338, -1162.622826425934,
//					-1654.1549133551766, -1512.137205771166, -790.835021992189, 123.467431466736 },
//			{ -6347.525478227298, -694.4033408647347, 1160.9406839554065, 971.8926594260364, 299.05271768359717,
//					109.52237127354402, 372.13838597422523, 345.4728447063311, -13.156264111602923, -564.5959611918406,
//					-817.0829689292871, -751.7117805073542, -393.93629210393726, 63.398391180796125 },
//			{ -6868.317528254065, -1233.196362558223, 881.5640169334124, 896.6978100096229, 400.4113905766186,
//					151.31228588045897, 180.98502264928004, 173.52348320254634, 91.07618838653526, 34.12445631513694,
//					23.10209025982499, 12.770749431180565, 6.226049960318813, 4.9698057137768945 },
//			{ -0.00912693043825783, -0.00984686862530531, -0.005224473621484113, -0.0014764310763611625,
//					0.0018077598776230293, 6.171919755201855E-4, -0.003727427804904315, -0.0031575156042558806,
//					0.0022337930511105086, 0.011722053537110241, 0.016310330017356132, 0.014771883873528534,
//					0.007685586510635121, -0.001195408730852784 },
//			{ -0.005695038212099737, -0.005445701563974296, -0.00271859373903653, -6.364691594875223E-4,
//					0.0012501710092316435, 7.336360790443912E-4, -0.0018697746530006857, -0.002059892903938969,
//					6.402959360715613E-4, 0.005841263268159357, 0.008503024400379405, 0.007904086303562617,
//					0.0042423969287474815, -4.725426547567621E-4 },
//			{ -3.5823116105829325, -0.6415596362719111, 0.46264427615629566, 0.4711954085902846, 0.21134117304211858,
//					0.0807199482525473, 0.0968288802874144, 0.09275268165227375, 0.048695549966508545,
//					0.018310215471106957, 0.012476434336725527, 0.006900087729922932, 0.0033759891246617412,
//					0.002729481995965842 },
//			{ 5.2142189751056116E-8, 6.140309826243028E-8, 3.4220794217415953E-8, 1.112782240933998E-8,
//					-9.118623084433187E-9, 1.7588626433327054E-9, 2.8554724609442854E-8, 1.5142739812899834E-8,
//					-2.8809385799648502E-8, -9.741027158340326E-8, -1.2737625321545192E-7, -1.103798179263543E-7,
//					-5.380088974333912E-8, 1.3920956462437725E-8 },
//			{ 5.2176993294169976E-8, 4.4640114297850717E-8, 2.0933438901426875E-8, 1.3537372981635042E-9,
//					-2.1823657205742136E-8, -2.093242811930728E-8, 1.7359645751760657E-8, 4.0166724205937515E-8,
//					1.7986141843388275E-8, -4.8665812979850374E-8, -9.209733178662827E-8, -9.906162086182046E-8,
//					-6.212632952352933E-8, -5.105146110597666E-9 },
//			{ 8.1874671702994E-4, 1.460978649199039E-4, -1.066655200551378E-4, -1.0883883761375421E-4,
//					-4.912382816011035E-5, -1.9044756239256498E-5, -2.2938187037567932E-5, -2.1946572873780555E-5,
//					-1.1527554087028702E-5, -4.357049915878548E-6, -2.9958091965990235E-6, -1.6575460950930352E-6,
//					-8.149456305783021E-7, -6.708684268805389E-7 } };

//	 n=2^20, with bias, smoothing factor 0.6
//	private final double[][] coeffMat = {
//			{ 1.942052510422544E8,4.037935320349598E7,-2.95552500018609E7,-3.696604216560283E7,-2.0259869194479216E7,-1.128970359960024E7,-1.3721872962612653E7,-9668531.706560997,5335318.356849767,2.6555101505692087E7,3.82532043195006E7,3.5834689440117806E7,2.0414010788177453E7,-675537.1722781733 },
//			{-5134.615637654703,-858.8645990675132,985.6657615749687,1087.857597862504,548.7905791447629,321.8298013683996,465.0940978639916,326.1434097673108,-243.43872617203942,-1064.9325394100813,-1520.1807785288302,-1420.0698404298003,-808.1563268685406,26.265504496754147},
//			{ -5662.98577463205,-1327.468335804232,715.3390041621819,998.8781271943411,581.4177526592387,312.4883110133359,327.4180658227599,232.80890613617635,-80.56274457700559,-513.0367025470579,-750.0412716885018,-705.7944828001815,-402.5599791326483,14.105092457498813 },
//			{ -6214.150737424855,-1806.3000354565156,443.4411036660058,912.5081380594046,618.5848406139438,307.43245448006655,191.02252341431844,137.8446932197902,80.51125372787192,39.319942201466596,22.73643852033684,12.13652925461447,6.246570131674552,3.915744098270425},
//			{ -0.00972869737547944,-0.00870225668952607,-0.00505594363510934,-0.0016912623413021712,5.745028526605903E-4,-2.5294741755838295E-4,-0.0026640574858114727,-0.0017062067857941544,0.0033039178037033838,0.010828205905859178,0.015016872889842258,0.013883638806578287,0.007858307767804877,-2.705313544052422E-4 },
//			{ -0.005956335529558929,-0.0048958885345214385,-0.002651784629353753,-7.070793104292554E-4,6.081570678531077E-4,1.81819438971753E-4,-0.0013540803800408375,-0.001215183145256375,0.001285270207220116,0.005374208143306316,0.00779525016441141,0.007394205261543444,0.004313681070974336,4.462121378197119E-5 },
//			{ -3.24096505259034,-0.9404826694686745,0.2340051117572386,0.47912776740245167,0.32528365557352557,0.16250955855501986,0.10191632156833688,0.07391303599421295,0.04325366768904295,0.02116363814951811,0.012266941618331069,0.006556790282189478,0.0033917073538003435,0.0021584058663139194 } ,
//			{5.635929754839824E-8,5.3674064900196363E-8,3.29292241878941E-8,1.2983002223148691E-8,-2.120972842403582E-10,6.60758968025189E-9,1.9817114659363932E-8,5.555322689710746E-9,-3.5443935365079365E-8,-9.043247362750483E-8,-1.1785304548351793E-7,-1.0445692364914729E-7,-5.5634050429382656E-8,7.482856765634109E-9},
//			{5.41197225756611E-8,4.077956985506274E-8,1.942678567288546E-8,-3.909121001270087E-11,-1.5406564399141658E-8,-1.1027300192055377E-8,1.4826581716064071E-8,2.8175053486625464E-8,6.86147756538277E-9,-4.460757314572059E-8,-8.342668370002254E-8,-9.065205547290023E-8,-6.130790536955921E-8,-1.2917548377046742E-8},
//			{7.40682040642583E-4,2.1441991624466508E-4,-5.436865622778506E-5,-1.1054921720098502E-4,-7.521446661998561E-5,-3.785672773730767E-5,-2.40517103147723E-5,-1.7564319856513783E-5,-1.0306704476914493E-5,-5.0569514781369834E-6,-2.9413967353477024E-6,-1.575152077907812E-6,-8.204789801536125E-7,-5.330685632526571E-7}
//	};

	// n=2^20, with bias, smoothing factor 0.4
//	private final double[][] coeffMat = {
//			{ 1.716358212866993E8,5.4386347994813204E7,-1.2088151710892923E7,-3.1960503172793403E7,-2.68522402008017E7,-1.8905051012978517E7,-1.4513304698083345E7,-6286716.45268519,8021428.47628509,2.4855596504078977E7,3.43892649877084E7,3.2634417225078654E7,2.0402269809537858E7,3120942.817125503 },
//			{ -4496.860338376982,-1272.0726209003988,492.9143759525982,963.4104647908424,766.6644316046951,550.125925921956,454.9857692942165,182.08082873044566,-348.608233958416,-994.8704129968585,-1364.2248793576841,-1291.6585251598783,-806.9335600539667,-123.5649215433591 },
//			{ -5034.364504637102,-1705.0500610300614,228.15479477814802,847.153700926649,742.4447776422325,515.4263377994313,372.81554227147785,173.20931785983262,-134.59876492533908,-481.5293646620642,-675.8495420341828,-643.7534653236744,-402.8239387886981,-61.22886161280013 },
//			{-5593.251247707525,-2148.6565399591964,-39.11501945359805,733.0048182310281,722.343097061299,484.5533884512181,292.1993263139236,163.6949460019557,78.43341373023917,32.27920856874525,14.671655288392586,7.266299162537894,4.337838051376668,3.488274766000311},
//			{ -0.009907697155961638,-0.00803234232431824,-0.0049477015158372,-0.002208200483708491,-5.070500411818498E-4,-7.226828633086068E-4,-0.0015859421489771877,-8.826677033861629E-5,0.004274693767409281,0.010073251586347625,0.013434075856690484,0.012604486534709546,0.007835095906369311,0.0011718225935679416},
//			{ -0.0059934708567822605,-0.004562257855428091,-0.002617288306877542,-9.821244044746688E-4,1.0268290985916695E-5,-1.3580533883683437E-4,-8.09296799668521E-4,-2.954915955333566E-4,0.001879035679151449,0.005003639580734816,0.006939072361908917,0.006674246779727312,0.0042761236840717105,8.302098199230992E-4},
//			{ -2.9169171225516752,-1.1190718943016726,-0.01785741749411507,0.38528619436483896,0.379469543551479,0.25509174680163527,0.15464215247631038,0.08727149924372352,0.042190028302606375,0.017584622856314153,0.008094321727473921,0.004028073986065264,0.002390323730596235,0.0018968070573897496 },
//			{5.7811906384052764E-8,4.9311702274892965E-8,3.230130676073035E-8,1.668705148449633E-8,6.991611496364489E-9,8.610178826516237E-9,1.1151731792113288E-8,-5.496252236672215E-9,-4.121401360553759E-8,-8.40263370080987E-8,-1.0607334111408321E-7,-9.559161680731949E-8,-5.603385933772677E-8,-3.054122094003334E-9},
//			{5.407811164991452E-8,3.7843044729613426E-8,1.8400829457491576E-8,1.924399438054771E-9,-8.26612396047063E-9,-4.430741323605182E-9,1.0859032889606668E-8,1.5877155369048162E-8,-3.450739896248905E-9,-4.2296329963568725E-8,-7.31167482278816E-8,-8.000125653163252E-8,-5.9269544065188556E-8,-2.3346620559911154E-8},
//			{6.665538407503852E-4,2.552450933589757E-4,3.251704849871733E-6,-8.90306813325814E-5,-8.762128789611434E-5,-5.908224604901515E-5,-3.6086717942837064E-5,-2.0575963597825886E-5,-1.0070329464862398E-5,-4.270000791478779E-6,-1.998918523604173E-6,-1.001219139079925E-6,-5.898607882321824E-7,-4.603068154208853E-7}
//	};

	// n=2^20, with bias, smoothing factor 0.2
	private final double[][] coeffMat = {
			{ 1.4392609712893954E8, 6.2929767076787174E7, 7545507.363809973, -1.946200857746634E7,
					-2.6020395905514643E7, -2.318813942150037E7, -1.6599428560099088E7, -5953097.829714188,
					7635428.043765783, 2.092228328288112E7, 2.8526301893678322E7, 2.8171683293844763E7,
					2.0709335200548537E7, 9696788.863131778 },
			{ -3726.355950082109, -1523.8613654967648, -50.92695052746876, 636.9284212079505, 772.147828363889,
					671.5949277568, 469.91462632502726, 115.52375383970673, -365.75807667175724, -853.5894863100162,
					-1134.0330991622882, -1110.875503255166, -812.728658580118, -376.5921634604461 },
			{ -4253.598613117425, -1935.5416561344564, -326.61345099328537, 479.8428196155654, 698.5767145969986,
					634.587024851364, 462.51804454685805, 204.69909726485156, -103.9442718374556, -393.64252488524147,
					-558.714132773922, -558.5059509755245, -413.37207308505936, -196.28505880941375 },
			{ -4799.970639628542, -2357.9247454494675, -606.1244399440684, 323.4448743569785, 627.9621035575743,
					600.8041211164945, 457.1404566473426, 294.5194124913793, 158.07147670957923, 67.1090022139581,
					18.35963771888693, -3.644769539127047, -11.251467381241298, -13.24556436332783 },
			{ -0.0097326786681118, -0.007639954903571208, -0.005153852995543419, -0.002974194957771671,
					-0.0014279216610157395, -7.368071327196226E-4, -1.3129579581583296E-4, 0.001790557980302845,
					0.005171483926606232, 0.009017833404505252, 0.011235818034894578, 0.010757024333022483,
					0.00775160309071842, 0.003465928285961758 },
			{ -0.005800577404883055, -0.0043523675054127076, -0.0027684676949691596, -0.001443365174113954,
					-5.519039073696623E-4, -2.233549989635872E-4, -5.539906409272267E-5, 7.747610786542359E-4,
					0.002457843620276057, 0.0045066353344150115, 0.00577784378949351, 0.005658338310426562,
					0.004195832382159802, 0.00204104227328155 },
			{ -2.5028460965197223, -1.228224099802549, -0.3138083828217443, 0.1714145883845602, 0.33019935256297395,
					0.31581312389231914, 0.24064991144600623, 0.15545078414832236, 0.08377363696107874,
					0.03586308446254641, 0.01010029204052721, -0.0016200012992723407, -0.005752840397659788,
					-0.006914473306405672 },
			{ 5.7536387723459344E-8, 4.715956040313927E-8, 3.3749963549081226E-8, 2.1479353382966942E-8,
					1.2149165438355568E-8, 6.814053352799812E-9, -3.7559238655985886E-10, -1.835018280888961E-8,
					-4.5921383388447296E-8, -7.463629638619121E-8, -8.91563839257072E-8, -8.225302478974781E-8,
					-5.617015612305699E-8, -2.064951722547672E-8 },
			{ 5.1076299688622255E-8, 3.5654195482257125E-8, 1.9871065686255457E-8, 7.186963809630712E-9,
					-3.809720621781918E-10, -4.250514769441705E-10, 3.895229072846575E-9, 1.5314202977148777E-9,
					-1.4104078917482775E-8, -3.938535786678662E-8, -5.99165470100572E-8, -6.625510624927954E-8,
					-5.6438986044010835E-8, -3.750460540536517E-8 },
			{ 5.718134362976775E-4, 2.801930066088844E-4, 7.096144122871998E-5, -4.006651338085942E-5,
					-7.634631167813392E-5, -7.29887231592491E-5, -5.573605374546585E-5, -3.6139468224405714E-5,
					-1.9589483589039657E-5, -8.48478519636933E-6, -2.484845726621001E-6, 2.7513127162687697E-7,
					1.2750567395411945E-6, 1.581219171876892E-6 } };

	/**
	 * Constructor for predefined values. This is only meant to be used for the
	 * example in our paper! Here, the coefficients are transformed using cubic
	 * smoothing splines.
	 * 
	 * @param coeffs     the coefficients for the sort.
	 * @param varIndices the variable indices identifying monomials.
	 * @param bias       whether we the coefficients contain a constant term or not.
	 * @param step       the step for which this sort is taken.
	 */
	public PKASort(double[] coeffs, int[][] varIndices, boolean bias, int step) {
		this.dimension = 6;
		this.coeffs = new double[coeffs.length];
		for (int i = 0; i < coeffs.length; i++)
			this.coeffs[i] = coeffMat[i][step - 1];

		this.bias = bias;
		this.varIndices = new int[varIndices.length][];
		for (int i = 0; i < varIndices.length; i++) {
			this.varIndices[i] = new int[varIndices[i].length];
			for (int j = 0; j < this.varIndices[i].length; j++)
				this.varIndices[i][j] = varIndices[i][j];
		}
	}

	/**
	 * Constructor for general example.
	 * 
	 * @param coeffs     The coefficients for the sort.
	 * @param varIndices the indices to identify the monomials.
	 * @param bias       whether the score function contains a constant term or not.
	 */
	public PKASort(double[] coeffs, int[][] varIndices, boolean bias) {
		this.dimension = 6;
		this.coeffs = new double[coeffs.length];
		for (int i = 0; i < coeffs.length; i++)
			this.coeffs[i] = coeffs[i];

		this.bias = bias;
		this.varIndices = new int[varIndices.length][];
		for (int i = 0; i < varIndices.length; i++) {
			this.varIndices[i] = new int[varIndices[i].length];
			for (int j = 0; j < this.varIndices[i].length; j++)
				this.varIndices[i][j] = varIndices[i][j];
		}
	}

	@Override
	public double scoreFunction(double[] v) {
		double score = 0.0;
		if (bias) {
			score = coeffs[0];
			for (int j = 0; j < varIndices.length; j++) {
				double temp = 1.0;
				for (int col : varIndices[j])
					temp *= v[col];
				score += coeffs[j + 1] * temp;
			}
		} else {
			for (int j = 0; j < varIndices.length; j++) {
				double temp = 1.0;
				for (int col : varIndices[j])
					temp *= v[col];
				score += coeffs[j] * temp;
			}
		}
		return score;
	}

	@Override
	public String toString() {
		return "PKA-Sort linear";
	}

}