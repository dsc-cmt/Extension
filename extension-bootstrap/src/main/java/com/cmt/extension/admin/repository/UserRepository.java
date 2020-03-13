package com.cmt.extension.admin.repository;

import com.cmt.extension.admin.model.entity.User;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author tuzhenxian
 * @date 19-10-26
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * @param sysFlag
     * @return
     */
    List<User> findBySysFlag(int sysFlag);

    /**
     * @param mobile
     * @param sysFlag
     * @return
     */
    List<User> findByUserMobileAndSysFlag(String mobile, int sysFlag);
}
