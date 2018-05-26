package backend.membership.domain

import library.eventsourcing.Repository

trait OrganizationRepository extends Repository[Organization, OrganizationId, OrganizationDomainEvent]
