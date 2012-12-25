<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Concept Sources" otherwise="/login.htm" redirect="/admin/concepts/conceptSource.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><openmrs:message code="ConceptSource.title"/></h2>

<c:if test="${isImplementationId}">
<br/><openmrs:message code="ConceptSource.isImplementationId"/><br/><br/>
</c:if>

<c:if test="${conceptSource.conceptSourceId == null}">
	<form method="post">
</c:if>

<fieldset>
<table>
	<tr>
		<td><openmrs:message code="general.name"/></td>
		<td>
			<spring:bind path="conceptSource.name">
				<input type="text" name="name" value="${status.value}" size="35" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
		<td class="description"><openmrs:message code="ConceptSource.name.help"/></td>
	</tr>
	<tr>
		<td><openmrs:message code="ConceptSource.hl7Code"/></td>
		<td>
			<spring:bind path="conceptSource.hl7Code">
				<input type="text" name="hl7Code" value="${status.value}" size="35" maxlength="20" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
		<td class="description"><openmrs:message code="ConceptSource.hl7Code.help"/></td>
	</tr>
	<tr>
		<td valign="top"><openmrs:message code="general.description"/></td>
		<td>
			<spring:bind path="conceptSource.description">
				<textarea name="description" rows="3" cols="40">${status.value}</textarea>
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
		<td class="description"><openmrs:message code="ConceptSource.description.help"/></td>
	</tr>
	<c:if test="${conceptSource.creator != null}">
		<tr>
			<td><openmrs:message code="general.createdBy" /></td>
			<td>
				${conceptSource.creator.personName} -
				<openmrs:formatDate date="${conceptSource.dateCreated}" type="long" />
			</td>
			<td class="description"></td>
		</tr>
	</c:if>
</table>
</fieldset>


<c:if test="${not conceptSource.retired && not empty conceptSource.conceptSourceId}">
<br/>
<fieldset>
	<form method="post">
			<h4><openmrs:message code="ConceptSource.retire"/></h4>
			
			<b><openmrs:message code="general.reason"/></b>
			<input type="text" value="" size="40" name="retireReason" />
			<spring:hasBindErrors name="conceptSource">
				<c:forEach items="${errors.allErrors}" var="error">
					<c:if test="${error.code == 'retireReason'}"><span class="error"><openmrs:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span></c:if>
				</c:forEach>
			</spring:hasBindErrors>
			<br/>
			<input type="submit" value='<openmrs:message code="ConceptSource.retire"/>' name="retire"/>
	</form>
</fieldset>
</c:if>

<c:if test="${conceptSource.voided && not empty conceptSource.conceptSourceId}">
<br/>
<fieldset>
	<form method="post">
			<h4><openmrs:message code="ConceptSource.restore"/></h4>
			<input type="submit" value='<openmrs:message code="ConceptSource.restore"/>' name="restore"/>
	</form>
</fieldset>
</c:if>

<c:if test="${not empty conceptSource.conceptSourceId }">
<br/>
<fieldset>
	<form method="post">
			<h4><openmrs:message code="ConceptSource.purge"/></h4>
			<input type="submit" value='<openmrs:message code="ConceptSource.purge"/>' name="purge"/>
	</form>
</fieldset>
</c:if>

<br />
<c:choose>
	<c:when test="${conceptSource.conceptSourceId == null}">
		<input type="submit" value='<openmrs:message code="ConceptSource.save"/>'>
		</form>
	</c:when>
	<c:otherwise>
		<openmrs:message code="ConceptSource.cannotBeEdited"/>
	</c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/template/footer.jsp" %>