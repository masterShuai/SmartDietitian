package cn.smartDietician.backEnd.domain.uionPK;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by wangshuai on 2016/4/15.
 */

@Embeddable
public class UserCookingUionPK implements Serializable {
    @Column
    private String userId;
    @Column
    private long cookingId;

    public UserCookingUionPK(String userId, long cookingId) {
        this.userId = userId;
        this.cookingId = cookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCookingId() {
        return cookingId;
    }

    public void setCookingId(long cookingId) {
        this.cookingId = cookingId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UserCookingUionPK){
            UserCookingUionPK pk=(UserCookingUionPK)obj;
            if(this.userId.trim().equals(pk.userId.trim())&&this.cookingId==pk.cookingId){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
