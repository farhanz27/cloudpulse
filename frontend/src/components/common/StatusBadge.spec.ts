import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import StatusBadge from './StatusBadge.vue'

describe('StatusBadge', () => {
  it('shows the status label and up styling when UP', () => {
    const wrapper = mount(StatusBadge, { props: { status: 'UP' } })
    expect(wrapper.text()).toBe('UP')
    expect(wrapper.find('.badge').classes()).toContain('badge-up')
  })

  it('shows down styling when DOWN', () => {
    const wrapper = mount(StatusBadge, { props: { status: 'DOWN' } })
    expect(wrapper.find('.badge').classes()).toContain('badge-down')
  })

  it('falls back to N/A and unknown styling when status is null', () => {
    const wrapper = mount(StatusBadge, { props: { status: null } })
    expect(wrapper.text()).toBe('N/A')
    expect(wrapper.find('.badge').classes()).toContain('badge-unknown')
  })
})
