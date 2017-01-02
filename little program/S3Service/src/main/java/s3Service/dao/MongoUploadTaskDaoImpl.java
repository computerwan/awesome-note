package s3Service.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import s3Service.UploadTask;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Wan on 2016/10/5 0005.
 */
public class MongoUploadTaskDaoImpl implements UploadTaskDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void prepareCollection(){
   		if(!this.mongoTemplate.collectionExists(UploadTask.class)){
   			this.mongoTemplate.createCollection(UploadTask.class);
   		}
   	}

    @Override
    public void addNewS3Task(UploadTask task) {
        mongoTemplate.insert(task);
    }

    @Override
    public UploadTask fetchS3Task() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -5);
        Date fiveMinutesAgo = c.getTime();

        Query query = new Query();
        query.addCriteria(
                new Criteria().orOperator(
                        Criteria.where("status").is(UploadTask.STATUS_ENUM.DEFAULT),
                        new Criteria().andOperator(
                                Criteria.where("status").is(UploadTask.STATUS_ENUM.PROCESSING),//五分钟前还在处理的任务说明挂掉了
                                Criteria.where("submitTime").lt(fiveMinutesAgo)
                        )
                )
        );

        Update update = new Update();
        update.set("status", UploadTask.STATUS_ENUM.PROCESSING);
        update.set("submitTime", new Date());

        return mongoTemplate.findAndModify(query, update, UploadTask.class);
    }

    @Override
    public void update(UploadTask task) {
        mongoTemplate.save(task);
    }

    @Override
    public void finishTask(UploadTask task) {
        mongoTemplate.remove(task);
    }
}
