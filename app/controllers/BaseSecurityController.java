package controllers;

import play.mvc.Security;
import security.Secured;

/**
 * Created by win7 on 2016/6/11.
 */
@Security.Authenticated(Secured.class)
public class BaseSecurityController extends BaseController {

}
