package wgn.myspringboot.mybatis;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2018/10/10
 * Time           :   下午6:22
 * Description    :
 */
public interface IMapper<T> extends Mapper<T>,MySqlMapper<T> {
}
