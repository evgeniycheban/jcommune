/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.jcommune.model.dao.hibernate;

import org.hibernate.criterion.Restrictions;
import org.jtalks.common.model.dao.hibernate.AbstractHibernateParentRepository;
import org.jtalks.common.model.entity.User;
import org.jtalks.jcommune.model.dao.UserDao;
import org.jtalks.jcommune.model.entity.JCUser;

import java.util.Collection;
import java.util.List;

/**
 * Hibernate implementation of UserDao.
 * Mainly intended for queering users from DB based on different criteria.
 *
 * @author Pavel Vervenko
 * @author Evgeniy Naumenko
 * @author Kirill Afonin
 * @author Anuar_Nurmakanov
 */
public class UserHibernateDao extends AbstractHibernateParentRepository<JCUser>
        implements UserDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public User getCommonUserByUsername(String username){
        return (User) getSession().getNamedQuery("getCommonUserByUsername")
                .setString("username", username).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public JCUser getByUsername(String username) {
        List<JCUser> users = getSession()
                .createCriteria(JCUser.class)
                .add(Restrictions.eq("username", username).ignoreCase())
                .list();
        if (users.size() == 1) {
            return users.get(0);
        } else {
            for (JCUser user : users) {
                if (user.getUsername().equals(username)) {
                    return user;
                }
            }
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JCUser getByEmail(String email) {
        return (JCUser) getSession()
                .createCriteria(JCUser.class)
                .add(Restrictions.eq("email", email))
                .setCacheable(true)
                .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<JCUser> getNonActivatedUsers() {
        return getSession()
                .createCriteria(JCUser.class)
                .add(Restrictions.eq("enabled", false))
                .setCacheable(false)
                .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JCUser getByUuid(String uuid) {
        return (JCUser) getSession()
                .createCriteria(JCUser.class)
                .add(Restrictions.eq("uuid", uuid))
                .setCacheable(true)
                .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JCUser> getByUsernames(List<String> usernames) {
        @SuppressWarnings("unchecked")
        List<JCUser> foundUsers = (List<JCUser>) getSession()
                .getNamedQuery("getByUsernames")
                .setParameterList("usernames", usernames)
                .list();
        return foundUsers;
    }
}
