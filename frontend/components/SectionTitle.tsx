export function SectionTitle({ title, action }: { title: string; action?: React.ReactNode }) {
  return (
    <div className="mb-4 flex items-center justify-between">
      <h2 className="text-2xl font-black text-gray-950">{title}</h2>
      {action}
    </div>
  );
}
