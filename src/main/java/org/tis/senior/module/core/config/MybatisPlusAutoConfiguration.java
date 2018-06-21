package org.tis.senior.module.core.config;

import com.baomidou.mybatisplus.mapper.ISqlInjector;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * 自动配置MybatisPlus
 * 注解 @AutoConfigureAfter 在 MultiDataSourceAutoConfiguration之后进行自动配置
 * </pre>
 *
 * @author stylefeng
 * @author Shiyunlai
 * @since 2017/5/20 21:58
 */
@Configuration
@MapperScan(basePackages = {"org.tis.senior.module.**.dao"})
public class MybatisPlusAutoConfiguration {

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 乐观锁mybatis插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    @ConditionalOnProperty(prefix = "tools", name = "mybatis-performance-open", havingValue = "true")
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * 注入sql注入器
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }
//
//    @Bean
//    public MetaObjectHandler metaObjectHandler() {
//        return new ToolsEntityMetaObjectHandler();
//    }

//    /**
//     * 数据范围mybatis插件
//     */
//    TODO 结合ABF对数据范围的控制设计实现
//    @Bean
//    public DataScopeInterceptor dataScopeInterceptor() {
//        return new DataScopeInterceptor();
//    }

}
