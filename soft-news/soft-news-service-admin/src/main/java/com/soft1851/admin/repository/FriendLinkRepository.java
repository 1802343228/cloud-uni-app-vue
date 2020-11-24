package com.soft1851.admin.repository;

import com.soft1851.pojo.mo.FriendLinkMO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author crq
 */
public interface FriendLinkRepository extends MongoRepository<FriendLinkMO,String> {
}
