package org.nopalsoft.mercury.web

import org.nopalsoft.mercury.domain.Project
import org.nopalsoft.mercury.domain.User
import javax.servlet.http.Cookie

class HomeController {

  def issueService
  def springSecurityService

  def index = {
    if (!session.project) {
      render(view: 'chooseProject', model: [projects: Project.findAll()])
    }else{
      def user = User.get(springSecurityService.principal.id)
      def model = new HashMap<String, ?>();
      model["issuesByStatus"] = issueService.getProjectStatusByStatus(session.project)
      model["issuesByAssignee"] = issueService.getProjectStatusByAssignee(session.project, "open")
      model["issuesByPriority"] = issueService.getProjectStatusByPriority(session.project, "open")
      model["openIssuesByPriority"] = issueService.getOpenIssuesByPriority(session.project, user)
      model["totalIssues"] = issueService.getTotalIssues(session.project)
      model
//      model.addAttribute("openIssues", issueManager.getTotalIssues(project, "open"));
    }
  }

  def changeProject = {
    session.project = Project.get(params.project)
    def cookie = new Cookie("projectId", params.project)
    cookie.maxAge = 60 * 60 * 60 * 24
    response.addCookie(cookie)
    redirect(action:'index')
  }
}