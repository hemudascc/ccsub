package net.bizao.json.bean;

import java.lang.reflect.Field;

public class ChallengeWrapper {
private Challenge challenge;

public Challenge getChallenge() {
	return challenge;
}

public void setChallenge(Challenge challenge) {
	this.challenge = challenge;
}
public String toString() {
	
    Field[] fields = this.getClass().getDeclaredFields();
    String str = this.getClass().getName();
    try {
        for (Field field : fields) {
            str += field.getName() + "=" + field.get(this) + ",";
        }
    } catch (IllegalArgumentException ex) {
        System.out.println(ex);
    } catch (IllegalAccessException ex) {
        System.out.println(ex);
    }
    return str;
}

}
