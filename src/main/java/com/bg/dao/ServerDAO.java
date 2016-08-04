package com.bg.dao;

import com.bg.model.Server;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
@Repository
@Mapper
public interface ServerDAO {

    String TABLE_NAME = "server";
    String INSERT_FIELDS = " name, ip, port, status ";
    String SELECT_FIELDS = " id, "+ INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values (#{name},#{ip},#{port},#{status})"})
    int addServer(Server server);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "order by id asc limit #{offset},#{limit}"})
    List<Server> getServerList(@Param("offset") int offset, @Param("limit") int limit);
}
