# durion-sales
Point of Sale Front-end component

## Purpose

Provide point-of-sale (POS) user experiences and supporting services for creating and managing customer transactions, coordinating with Pricing, Inventory, CRM, Billing, and Order domains to produce auditable sales outcomes.

## Scope

In scope:
- POS flows for building a sale (customer selection, items/services, pricing visibility)
- Promotion application and pricing snapshot visibility (as provided by Pricing)
- Checkout initiation and invoice/payment orchestration surfaces (via Billing integrations)
- Operational actions such as order cancellation requests with reason capture (Order domain orchestrates)

Out of scope:
- Authoritative pricing/tax calculation logic (owned by Pricing/Billing)
- Payment gateway settlement and refund mechanics (owned by Billing adapters)
- Inventory valuation and GL posting (owned by Inventory/Accounting)
