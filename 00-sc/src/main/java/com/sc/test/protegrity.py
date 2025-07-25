


from ptapi import ProtectorFactory

# 初始化
factory = ProtectorFactory.get_instance()
protector = factory.create('/opt/protegrity/policy.ptcfg')

# 使用
encrypted = protector.encrypt('customer_name', 'John Doe')
print("Encrypted:", encrypted)



