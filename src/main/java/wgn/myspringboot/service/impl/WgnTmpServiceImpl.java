package wgn.myspringboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wgn.myspringboot.bussiness.entity.WgnTmpEntity;
import wgn.myspringboot.bussiness.mapper.WgnTmpSlaveMapper;
import wgn.myspringboot.primary.mapper.WgnTmpMasterMapper;
import wgn.myspringboot.service.WgnTmpService;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2018/10/10
 * Time           :   下午6:54
 * Description    :
 */
@Service
public class WgnTmpServiceImpl implements WgnTmpService {
    @Autowired
    private WgnTmpSlaveMapper wgnTmpSlaveMapper;

    @Autowired
    private WgnTmpMasterMapper wgnTmpMasterMapper;


    @Transactional
    public void  testTransaction(){

        WgnTmpEntity wgnTmpEntity=new WgnTmpEntity();

        wgnTmpEntity.setId(2L);
        wgnTmpEntity.setName("wahaha");

        wgnTmpMasterMapper.insert(wgnTmpEntity);


        wgnTmpEntity.setId(1L);
        wgnTmpEntity.setName("haha");
        wgnTmpSlaveMapper.insert(wgnTmpEntity);
    }


}
