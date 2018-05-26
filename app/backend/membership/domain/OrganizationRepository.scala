package backend.membership.domain

import backend.common.types.OrganizationId
import library.eventsourcing.Repository

trait OrganizationRepository extends Repository[Organization, OrganizationId, OrganizationDomainEvent]
