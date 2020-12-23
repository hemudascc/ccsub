package net.mycomp.mt2.ksa;

import org.springframework.util.StringUtils;

public class Mt2KsaTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//request: http://mt2-sa.com:1000/api/dob/CollectCent/ZAINSA/SubPIN/18 :
			
		
		//{Authorization=WSSE realm=MT2,profile=UsernameToken,
			//amount=1, Connection=Keep-Alive, 
			//description=Kido Kingdom, Host=mt2-sa.com:1000, Accept-Encoding=gzip,deflate,
			//x-user-id=966598649485, X-WSSE=UsernameToken Username="2500118",
			//PasswordDigest="g1YZ80KuTeOckJTKdgzy4Vba8/w=",Nonce="MT2PAYKSASUBPIN"
			//,Created="2020-01-23T03:39:46.197+0530", Content-Type=application/json}
			
			String genratePaswordDigest=Mt2KSAServiceApi.generatePaswordHash(Mt2KSAConstant.NOUNCE_PIN,"2020-01-23T03:39:46.197+0530",
					Mt2KSAServiceApi.getSha1Hash("xdoKVEfA")
		, "BNvyqLdi7Y206SlERn5ftaLVE8eNV4z7jBJxbMM597Dqknhfrjt88zG1B9tS6ZWCZuh5Io2EaAFW7VaRqyVbb1ykuivCTuyKKA1qGM7DRpx8ujufyb27Dl1xvwaZs0lgBvM6ukKd1SUm30pKoQDh21GY93vJcHKqDR6y7YkO1oGl1e44HZbbWDOapnw1pBBTH3iMurdZ6pQdVUxuy0CM684ADwJj1SY0nmdxwSaPlLF7naO4qB3Yyon8ziIxiuyN0qq8hAyfpn0U3xM2hetZpA1yzFyOJd7rNsGxjfnpDn8LX2FC5gPDB8Y0Ys9cCk6MP9GaqPjBrDWB1Pa6zvj3X3qe4g8rGLnSRWvoW5LPwNLxMWzJRDcGBCpmi2GKhrvgPDkETmK4k36JRt2HhZaD8hRP6JGtZA7FS5hIcup4sIV75g4pzy3SRsI8m58KgQFjw1rmLU5n9FK0VjjqeQt17PoZXJVHVeocDcbEoief2giLiiMClh6Z");
	  System.out.println("genratePaswordDigest :: "+genratePaswordDigest);
	  String expectd="tprV1TrFDG1ftDVrgsA05HFY34I=";
	  System.out.println("ExpectedPaswordDigest :: "+expectd);
	  System.out.println("equeal :: "+expectd.equals(genratePaswordDigest));
	  
	  System.out.println("msisdn is empty "+StringUtils.isEmpty("123"));
	  
	
	  
	}

}
