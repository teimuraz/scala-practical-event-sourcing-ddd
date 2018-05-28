package backend.membership.domain

import backend.common.types.organization.OrganizationId
import backend.membership.api.event.OrganizationEvent
import library.eventsourcing.Repository

trait OrganizationRepository extends Repository[Organization, OrganizationId, OrganizationEvent]
